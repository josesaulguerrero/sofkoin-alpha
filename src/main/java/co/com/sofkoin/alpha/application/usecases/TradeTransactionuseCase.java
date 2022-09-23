package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.user.commands.CommitP2PTransaction;
import co.com.sofkoin.alpha.domain.user.commands.CommitTradeTransaction;
import co.com.sofkoin.alpha.domain.user.entities.root.User;
import co.com.sofkoin.alpha.domain.user.events.P2PTransactionCommitted;
import co.com.sofkoin.alpha.domain.user.events.TradeTransactionCommitted;
import co.com.sofkoin.alpha.domain.user.values.Cash;
import co.com.sofkoin.alpha.domain.user.values.Timestamp;
import co.com.sofkoin.alpha.domain.user.values.TransactionCryptoAmount;
import co.com.sofkoin.alpha.domain.user.values.TransactionCryptoPrice;
import co.com.sofkoin.alpha.domain.user.values.TransactionTypes;
import co.com.sofkoin.alpha.domain.user.values.identities.TransactionID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
@Slf4j
@Component
@AllArgsConstructor
public class TradeTransactionuseCase implements UseCase<CommitTradeTransaction> {

    private final DomainEventRepository repository;
    private final DomainEventBus bus;


    @Override
    public Flux<DomainEvent> apply(Mono<CommitTradeTransaction> tradetransactioncommand) {

        return  tradetransactioncommand.flatMapMany(command -> repository.findByAggregateRootId(command.getBuyerId())
                .collectList()
                .flatMapIterable(events ->{
                    User user = User.from(new UserID(command.getBuyerId()), events);
                    user.commitTradeTransaction(new TransactionID(),
                            new UserID(command.getBuyerId()),
                            TransactionTypes.BUY,
                            new CryptoSymbol(command.getCryptoSymbol()),
                            new TransactionCryptoAmount(command.getCryptoAmount()),
                            new TransactionCryptoPrice(command.getCryptoPrice()),
                            new Cash(command.getCash()),
                            new Timestamp());
                    log.info("User transaction running");
                    return user.getUncommittedChanges();
                })
                .map(domainEvent -> {
                    bus.publishEvent(domainEvent);
                    return domainEvent;
                })
                .flatMap(repository::saveDomainEvent)

        );
    }
}
