package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
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
    public Flux<DomainEvent> apply(Mono<CommitP2PTransaction> P2Ptransactioncommand) {

        Flux<DomainEvent> buyerFlux = P2Ptransactioncommand
                .flatMapMany(command -> P2PTransaction(P2Ptransactioncommand, command.getBuyerId(), TransactionTypes.BUY));

        Flux<DomainEvent> sellerFlux = P2Ptransactioncommand
                .flatMapMany(command -> P2PTransaction(P2Ptransactioncommand, command.getSellerId(), TransactionTypes.SELL));

        return Flux.merge(buyerFlux,sellerFlux);
    }

    public Flux<DomainEvent> P2PTransaction(Mono<CommitP2PTransaction> P2Ptransactioncommand, String userId, TransactionTypes transactionType) {

    return P2Ptransactioncommand.flatMapMany(command ->
            repository.findByAggregateRootId(userId)
                    .collectList()
                    .flatMapIterable(events ->{
                        User seller = User.from(new UserID(userId), events);
                        seller.commitP2PTransaction(new TransactionID(),
                                new UserID(command.getSellerId()),
                                new UserID(command.getBuyerId()),
                                new CryptoSymbol(command.getCryptoSymbol()),
                                new TransactionCryptoAmount(command.getCryptoAmount()),
                                new TransactionCryptoPrice(command.getCryptoPrice()),
                                transactionType.name(),
                                new Cash(command.getCash()),
                                new Timestamp());

                        log.info(transactionType.name() + " transaction running for User: " + seller);

                        return seller.getUncommittedChanges();
                    }).map(domainEvent -> {
                        log.info(domainEvent.toString());
                  //      bus.publishEvent(domainEvent);
                        return domainEvent;
                    }).flatMap(event -> repository.saveDomainEvent(event)));

    }



    /*      Flux<DomainEvent>  firstFlux =  P2Ptransactioncommand.flatMapMany(command ->
                repository.findByAggregateRootId(command.getBuyerId())
                .collectList()
                .flatMapIterable(events ->{
                    User buyer = User.from(new UserID(command.getBuyerId()), (List<DomainEvent>) events);
                    buyer.commitP2PTransaction(new TransactionID(),
                            new UserID(command.getSellerId()),
                            new UserID(command.getBuyerId()),
                            new CryptoSymbol(command.getCryptoSymbol()),
                            new TransactionCryptoAmount(command.getCryptoAmount()),
                            new TransactionCryptoPrice(command.getCryptoPrice()),
                            TransactionTypes.BUY.name(),
                            new Cash(command.getCash()),
                           new Timestamp());
                    log.info("Buyer transaction runing");

                    return buyer.getUncommittedChanges();
                }).map(domainEvent -> {
            bus.publishEvent(domainEvent);
            return domainEvent;
        }).flatMap(event -> repository.saveDomainEvent(event)));


        Flux<DomainEvent> secondFlux =  P2Ptransactioncommand.flatMapMany(command ->
                repository.findByAggregateRootId(command.getSellerId())
                .collectList()
                .flatMapIterable(events ->{
                    User seller = User.from(new UserID(command.getSellerId()), (List<DomainEvent>) events);
                    seller.commitP2PTransaction(new TransactionID(),
                            new UserID(command.getSellerId()),
                            new UserID(command.getBuyerId()),
                            new CryptoSymbol(command.getCryptoSymbol()),
                            new TransactionCryptoAmount(command.getCryptoAmount()),
                            new TransactionCryptoPrice(command.getCryptoPrice()),
                            TransactionTypes.SELL.name(),
                            new Cash(command.getCash()),
                            new Timestamp());

                    log.info("Seller transaction runing");

                    return seller.getUncommittedChanges();
                }).map(domainEvent -> {
                    bus.publishEvent(domainEvent);
                    return domainEvent;
                }).flatMap(event -> repository.saveDomainEvent(event)));

*/

}
