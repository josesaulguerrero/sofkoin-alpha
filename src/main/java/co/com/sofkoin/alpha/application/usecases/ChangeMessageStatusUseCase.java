package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.market.commands.PublishP2POffer;
import co.com.sofkoin.alpha.domain.user.commands.ChangeMessageStatus;
import co.com.sofkoin.alpha.domain.user.entities.Message;
import co.com.sofkoin.alpha.domain.user.entities.root.User;
import co.com.sofkoin.alpha.domain.user.values.MessageStatus;
import co.com.sofkoin.alpha.domain.user.values.identities.MessageID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Service
@AllArgsConstructor
public class ChangeMessageStatusUseCase implements UseCase<ChangeMessageStatus> {

    private final DomainEventBus bus;
    private final DomainEventRepository repository;
    private final PublishP2POfferUseCase publishP2POfferUseCase;

    @Override
    public Flux<DomainEvent> apply(Mono<ChangeMessageStatus> changeMessageStatusCommand) {

        Flux<DomainEvent> receiverFLux = changeMessageStatusCommand
                .flatMapMany(command -> applyCommandToUserById(command, command.getReceiverId()));
        Flux<DomainEvent> senderFLux = changeMessageStatusCommand
                .flatMapMany(command -> applyCommandToUserById(command, command.getSenderId()));

        return Flux.merge(receiverFLux, senderFLux);
    }

    private Flux<DomainEvent> applyCommandToUserById(ChangeMessageStatus command, String userId) {

        return repository.findByAggregateRootId(userId)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new IllegalArgumentException("User id: " + command.getReceiverId() + " not found.")))
                )
                .collectList()
                .map(events -> User.from(new UserID(userId), events))
                .map(user -> {
                    MessageStatus messageStatus = MessageStatus.valueOf(command.getNewStatus().toUpperCase(Locale.ROOT).trim());
                    if (messageStatus.equals(MessageStatus.ACCEPTED) && user.identity().value().equals(command.getReceiverId())) {
                        Message message = user.findMessageById(command.getMessageId());
                        Double cryptoAmountBySymbol =
                                user.findCryptoAmountBySymbol(message.cryptoSymbol().value());
                        validateReceiverCryptoAmount(user, message.identity().value(), cryptoAmountBySymbol);
                    }

                    return user;
                })
                .flatMapIterable(user -> {
                    Message message = user.findMessageById(command.getMessageId());

                    if (message.messageStatus().equals(MessageStatus.ACCEPTED) || message.messageStatus().equals(MessageStatus.REJECTED)) {
                        throw new IllegalArgumentException("The message status already changed; you cannot update it anymore.");
                    }

                    user.changeMessageStatus(
                            new UserID(command.getReceiverId()),
                            new UserID(command.getSenderId()),
                            new MessageID(command.getMessageId()),
                            message.messageRelationType(),
                            MessageStatus.valueOf(command.getNewStatus().toUpperCase(Locale.ROOT).trim())
                    );

                    if (user.identity().value().equals(message.receiverId().value()) && command.getNewStatus().equals(MessageStatus.ACCEPTED.name())) {
                        PublishP2POffer publishP2POffer = new PublishP2POffer(
                                message.marketID().value(),
                                command.getReceiverId(),
                                command.getSenderId(),
                                message.cryptoSymbol().value(),
                                message.proposalCryptoAmount().value(),
                                message.proposalCryptoPrice().value()
                        );
                        publishP2POfferUseCase.apply(Mono.just(publishP2POffer)).subscribe();
                    }
                    return user.getUncommittedChanges();
                })
                .flatMap(this.repository::saveDomainEvent)
                .doOnNext(this.bus::publishEvent);
    }

    private void validateReceiverCryptoAmount(User user, String messageId, Double cryptoAmountBySymbol) {
        Message message = user.findMessageById(messageId);
        if (message.proposalCryptoAmount().value() > cryptoAmountBySymbol) {
            this.rejectOfferMessage(user, message);
            throw new IllegalArgumentException(
                    String.format(
                            "You don't have the enough amount of %s to accept this proposal.",
                            message.cryptoSymbol().value()
                    ));
        }
    }

    private void rejectOfferMessage(User user, Message message) {
        user.changeMessageStatus(
                message.receiverId(),
                message.senderId(),
                message.identity(),
                message.messageRelationType(),
                MessageStatus.REJECTED
        );
        Mono.just(user.getUncommittedChanges())
                .flatMapIterable(events -> events)
                .flatMap(this.repository::saveDomainEvent)
                .doOnNext(this.bus::publishEvent);
    }
}
