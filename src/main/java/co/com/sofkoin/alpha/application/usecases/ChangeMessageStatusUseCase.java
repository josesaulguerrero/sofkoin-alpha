package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.user.commands.ChangeMessageStatus;
import co.com.sofkoin.alpha.domain.user.entities.root.User;
import co.com.sofkoin.alpha.domain.user.values.MessageStatus;
import co.com.sofkoin.alpha.domain.user.values.identities.MessageID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Locale;

@AllArgsConstructor
@Service
public class ChangeMessageStatusUseCase implements UseCase<ChangeMessageStatus>{

    private final DomainEventBus bus;
    private final DomainEventRepository repository;

    @Override
    public Flux<DomainEvent> apply(Mono<ChangeMessageStatus> changeMessageStatusCommand) {

        Flux<DomainEvent> receiverFLux = changeMessageStatusCommand
                .flatMapMany(command -> applyCommandToUserById(command, command.getReceiverId()));
        Flux<DomainEvent> senderFLux = changeMessageStatusCommand
                .flatMapMany(command -> applyCommandToUserById(command, command.getSenderId()));

        return Flux.merge(receiverFLux, senderFLux);
    }

    private Flux<DomainEvent> applyCommandToUserById(ChangeMessageStatus command, String userId){

        return repository.findByAggregateRootId(userId)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new Throwable("User id: " + command.getReceiverId() + " not found.")))
                )
                .collectList()
                .flatMapIterable(events -> {
                    User user = User.from(new UserID(userId), events);
                    user.changeMessageStatus(new UserID(command.getReceiverId()),
                            new UserID(command.getSenderId()),
                            new MessageID(command.getMessageId()),
                            MessageStatus.valueOf(command.getNewStatus().toUpperCase(Locale.ROOT).trim())
                    );
                    return user.getUncommittedChanges();
                }).flatMap(event -> {
                    bus.publishEvent(event);
                    return repository.saveDomainEvent(event).thenReturn(event);
                });
    }

}