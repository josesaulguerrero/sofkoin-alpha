package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.user.commands.FundWallet;
import co.com.sofkoin.alpha.domain.user.entities.root.User;
import co.com.sofkoin.alpha.domain.user.values.Cash;
import co.com.sofkoin.alpha.domain.user.values.Timestamp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FundWalletUseCase implements UseCase<FundWallet> {

    private final DomainEventRepository repository;
    private final DomainEventBus bus;

    @Override
    public Flux<DomainEvent> apply(Mono<FundWallet> commandMono) {
        return commandMono.flatMapMany(command -> repository.findByAggregateRootId(command.getUserId())
                .collectList()
                .flatMapIterable(domainEvents -> {
                    var userID = new UserID(command.getUserId());
                    User user = User.from(userID, (List<DomainEvent>) domainEvents);
                    user.fundWallet(userID, new Cash(command.getCashAmount()), new Timestamp());
                    return user.getUncommittedChanges();
                })
                .map(domainEvent -> {
                    bus.publishEvent(domainEvent);
                    log.info("Wallet Funded Domain Event published");
                    return domainEvent;
                })
                .flatMap(repository::saveDomainEvent)
        );
    }
}
