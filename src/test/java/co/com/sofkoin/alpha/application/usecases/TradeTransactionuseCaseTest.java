package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.user.commands.CommitTradeTransaction;
import co.com.sofkoin.alpha.domain.user.events.TradeTransactionCommitted;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import co.com.sofkoin.alpha.domain.user.events.WalletFunded;
import co.com.sofkoin.alpha.domain.user.values.Timestamp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TradeTransactionuseCaseTest {
    @Mock
    private DomainEventRepository eventBus;
    @Mock
    private DomainEventRepository domainEventRepository;

    @InjectMocks
    private TradeTransactionuseCase useCase;

    @Test
    void tradeTransactionuseCase() {

        CommitTradeTransaction command = new CommitTradeTransaction(
                "1",
                "BUY",
                "ETH",
                1.0,
                1.0,
                1.0
        );

        TradeTransactionCommitted event = new TradeTransactionCommitted(
                "33333",
                "1",
                "BUY",
                "ETH",
                1.0,
                1.0,
                1.0,
                new Timestamp().toString()
        );
        var user = new UserSignedUp(
                "1",
                "maxmusterman@daad.de",
                "maxmusterman@daad.de",
                "Max",
                "Musterman",
                "0123456789",
                "http://www.acatar.url.com",
                "GMAIL"
        );

        var walletfunded = new WalletFunded(
                "1",
                100.0,
                new Timestamp().toString()
                );

       BDDMockito.when(this.domainEventRepository.findByAggregateRootId(BDDMockito.anyString()))
                .thenReturn(Flux.just(user
                        ,walletfunded
                ));

        BDDMockito
                .when(this.domainEventRepository.saveDomainEvent(ArgumentMatchers.any(DomainEvent.class)))
                .thenReturn(Mono.just(event));


        Mono<List<DomainEvent>> triggeredevents = this.useCase.apply(Mono.just(command))
                .collectList();

        StepVerifier.create(triggeredevents)
                .expectSubscription()
                .expectNextMatches(domainEvents ->
                        domainEvents.size() == 1 &&
                        domainEvents.get(0) instanceof TradeTransactionCommitted)
                .verifyComplete();

    //    BDDMockito.verify(this.eventBus, BDDMockito.times(1))
    //            .saveDomainEvent(ArgumentMatchers.any(DomainEvent.class));

        BDDMockito.verify(this.domainEventRepository, BDDMockito.times(1))
                .saveDomainEvent(ArgumentMatchers.any(DomainEvent.class));

    }
}