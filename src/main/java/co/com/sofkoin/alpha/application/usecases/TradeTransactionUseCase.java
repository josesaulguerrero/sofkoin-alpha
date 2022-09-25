package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.user.commands.CommitTradeTransaction;
import co.com.sofkoin.alpha.domain.user.entities.root.User;
import co.com.sofkoin.alpha.domain.user.values.*;
import co.com.sofkoin.alpha.domain.user.values.identities.TransactionID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Slf4j
@Component
@AllArgsConstructor
public class TradeTransactionUseCase implements UseCase<CommitTradeTransaction> {

    private final DomainEventRepository repository;
    private final DomainEventBus bus;


    @Override
    public Flux<DomainEvent> apply(Mono<CommitTradeTransaction> tradeTransactionCommand) {

        return  tradeTransactionCommand.flatMapMany(command -> repository.findByAggregateRootId(command.getBuyerId())
                .collectList()
                .doOnNext(events ->
                        repository
                        .findByAggregateRootId(command.getBuyerId())
                        .collectList()
                        .doOnNext(userEvents -> {
                          User user = User.from(new UserID(command.getBuyerId()), userEvents);

                          TransactionTypes transactionTypes =
                                  TransactionTypes.valueOf(
                                          command.getTransactionType().toUpperCase(Locale.ROOT).trim()
                                  );

                          if (TransactionTypes.BUY.equals(transactionTypes)) {
                            user.validateBuyTransaction(command.getCash());

                          } else if (TransactionTypes.SELL.equals(transactionTypes)) {
                            user.validateSellTransaction(command.getCryptoAmount(), command.getCryptoSymbol());

                          } else
                            throw new IllegalArgumentException("The given transaction type is not allowed.");

                        }))
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
