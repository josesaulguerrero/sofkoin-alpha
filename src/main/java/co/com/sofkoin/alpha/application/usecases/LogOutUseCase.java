package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.user.commands.LogOut;
import co.com.sofkoin.alpha.domain.user.entities.root.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LogOutUseCase implements UseCase<LogOut> {

    private final DomainEventRepository repository;
    private final DomainEventBus bus;


    @Override
    public Flux<DomainEvent> apply(Mono<LogOut> commandMono) {
        return commandMono.flatMapMany(command -> repository.findByAggregateRootId(command.getUserId())
                .collectList()
                .flatMapIterable(domainEvents -> {
                    var userID = new UserID(command.getUserId());
                    User user = User.from(userID, (List<DomainEvent>) domainEvents);
                    user.logOut(userID);
                    return user.getUncommittedChanges();
                })
                .map(domainEvent -> {
                    bus.publishEvent(domainEvent);
                    log.info("Logged Out Domain Event published");
                    return domainEvent;
                })
                .flatMap(repository::saveDomainEvent)
        );
    }
}
