package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.market.values.identities.MarketID;
import co.com.sofkoin.alpha.domain.market.values.identities.OfferId;
import co.com.sofkoin.alpha.domain.user.commands.CommitP2PTransaction;
import co.com.sofkoin.alpha.domain.user.entities.root.User;
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
public class P2PTransactionUseCase implements UseCase<CommitP2PTransaction> {

    private final DomainEventRepository repository;
    private final DomainEventBus bus;

    @Override
    public Flux<DomainEvent> apply(Mono<CommitP2PTransaction> P2PTransactionCommand) {

        Flux<DomainEvent> buyerFlux = P2PTransactionCommand
                .flatMapMany(command -> P2PTransaction(P2PTransactionCommand, command.getBuyerId(), TransactionTypes.BUY));

        Flux<DomainEvent> sellerFlux = P2PTransactionCommand
                .flatMapMany(command -> P2PTransaction(P2PTransactionCommand, command.getSellerId(), TransactionTypes.SELL));

        return Flux.merge(buyerFlux,sellerFlux);
    }

    public Flux<DomainEvent> P2PTransaction(Mono<CommitP2PTransaction> P2PTransactionCommand, String userId, TransactionTypes transactionType) {

    return P2PTransactionCommand.flatMapMany(command ->
            repository.findByAggregateRootId(userId)
                    .collectList()
                    .map(events -> User.from(new UserID(userId), events))
                    .doOnNext(user -> {
                          if(TransactionTypes.BUY.equals(transactionType)) {
                            user.validateBuyTransaction(command.getCryptoAmount() * command.getCryptoPrice());

                          } else if(TransactionTypes.SELL.equals(transactionType)) {
                            user.validateSellTransaction(command.getCryptoAmount(), command.getCryptoSymbol());

                          } else throw new IllegalArgumentException("The given transaction type is not allowed.");

                    })
                    .flatMapIterable(user -> {

                      user.commitP2PTransaction(new TransactionID(),
                                new UserID(command.getSellerId()),
                                new UserID(command.getBuyerId()),
                                new OfferId(command.getOfferId()),
                                new MarketID(command.getMarketId()),
                                new CryptoSymbol(command.getCryptoSymbol()),
                                new TransactionCryptoAmount(command.getCryptoAmount()),
                                new TransactionCryptoPrice(command.getCryptoPrice()),
                                transactionType.name(),
                                new Cash(command.getCryptoAmount() * command.getCryptoPrice()),
                                new Timestamp());

                        log.info(transactionType.name() + " transaction running for User: " + user);
                        return user.getUncommittedChanges();
                    }).map(domainEvent -> {
                        bus.publishEvent(domainEvent);
                        return domainEvent;
                    }).flatMap(repository::saveDomainEvent));

    }


}
