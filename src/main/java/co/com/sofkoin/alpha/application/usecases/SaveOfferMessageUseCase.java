package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.market.values.identities.MarketID;
import co.com.sofkoin.alpha.domain.user.commands.SaveOfferMessage;
import co.com.sofkoin.alpha.domain.user.entities.root.User;
import co.com.sofkoin.alpha.domain.user.values.MessageRelationTypes;
import co.com.sofkoin.alpha.domain.user.values.TransactionCryptoAmount;
import co.com.sofkoin.alpha.domain.user.values.TransactionCryptoPrice;
import co.com.sofkoin.alpha.domain.user.values.identities.MessageID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class SaveOfferMessageUseCase implements UseCase<SaveOfferMessage> {

    private final DomainEventRepository repository;
    private final DomainEventBus bus;

    @Override
    public Flux<DomainEvent> apply(Mono<SaveOfferMessage> saveOfferMessageCommand) {

        MessageID messageId = new MessageID();
        Flux<DomainEvent> receiverFLux = saveOfferMessageCommand
                .flatMapMany(command -> applyCommandToUserById(command, command.getReceiverId(), messageId, MessageRelationTypes.RECEIVER));
        Flux<DomainEvent> senderFLux = saveOfferMessageCommand
                .flatMapMany(command -> applyCommandToUserById(command, command.getSenderId(), messageId, MessageRelationTypes.SENDER));
        return Flux.merge(receiverFLux, senderFLux);
    }

    private Flux<DomainEvent> applyCommandToUserById(SaveOfferMessage command, String userId, MessageID messageId, MessageRelationTypes messageRelationType) {

        return repository.findByAggregateRootId(userId)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new IllegalArgumentException("User id: " + command.getReceiverId() + " not found.")))
                )
                .collectList()
                .map(events ->  User.from(new UserID(userId), events))
                .map(user -> {
                    if (command.getReceiverId().equals(userId)) {
                        this.validateReceiverCryptos(
                                command.getCryptoAmount(),
                                user.findCryptoAmountBySymbol(command.getCryptoSymbol())
                        );
                    }
                    return user;
                })
                .flatMapIterable(user -> {
                    user.saveOfferMessage(
                            messageId,
                            new MarketID(command.getMarketId()),
                            new UserID(command.getSenderId()),
                            new UserID(command.getReceiverId()),
                            new CryptoSymbol(command.getCryptoSymbol()),
                            messageRelationType,
                            new TransactionCryptoAmount(command.getCryptoAmount()),
                            new TransactionCryptoPrice(command.getCryptoPrice())
                    );
                    return user.getUncommittedChanges();
                })
                .flatMap(this.repository::saveDomainEvent)
                .doOnNext(this.bus::publishEvent);
    }

    private void validateReceiverCryptos(Double proposalCryptoAmount, Double userAvailableCryptoAmount) {
        if (proposalCryptoAmount > userAvailableCryptoAmount) {
            throw new IllegalArgumentException("The receiver doesn't have enough cryptos, you can't send the offer message.");
        }
    }

}
