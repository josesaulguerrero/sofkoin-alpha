package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.user.commands.CommitP2PTransaction;
import co.com.sofkoin.alpha.domain.user.commands.CommitTradeTransaction;
import co.com.sofkoin.alpha.domain.user.events.P2PTransactionCommitted;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class P2PTransactionUseCaseTest {

    @Mock
    private DomainEventRepository eventBus;
    @Mock
    private DomainEventRepository domainEventRepository;

    @InjectMocks
    private P2PTransactionUseCase useCase;

    @Test
    void P2PTransactionUseCase() {
        //Command
        CommitP2PTransaction command = new CommitP2PTransaction(
                "2",
                "1",
                "ETH",
                1.0,
                1.0,
                "SELL",
                1.0
        );

        //events
        P2PTransactionCommitted eventseller = new P2PTransactionCommitted(
                "33333",
                "2",
                "1",
                "ETH",
                1.0,
                1.0,
                "SELL",
                1.0,
                new Timestamp().toString()
        );
        P2PTransactionCommitted eventbuyer = new P2PTransactionCommitted(
                "33333",
                "2",
                "1",
                "ETH",
                1.0,
                1.0,
                "BUY",
                1.0,
                new Timestamp().toString()
        );

        //usersignedupevents
        var buyeruser = new UserSignedUp(
                "1",
                "maxmusterman@daad.de",
                "maxmusterman@daad.de",
                "Max",
                "Musterman",
                "0123456789",
                "http://www.acatar.url.com",
                "GMAIL"
        );
        var selleruser = new UserSignedUp(
                "2",
                "clara@daad.de",
                "clara@daad.de",
                "Clara",
                "Musterfrau",
                "0123456789",
                "http://www.acatar.url.com",
                "GMAIL"
        );
        //add money to buyer
        var walletfundedbuyer = new WalletFunded(
                "1",
                100.0,
                new Timestamp().toString()
        );
        //add money to seller
        var walletfundedseller = new WalletFunded(
                "2",
                100.0,
                new Timestamp().toString()
        );
        //add crypto to seller
        var sellerbuycoin = new TradeTransactionCommitted(
                "11",
                "2",
                "BUY",
                "ETH",
                4.0,
                1.0,
                1.0,
                new Timestamp().toString()
        );

        BDDMockito.when(this.domainEventRepository.findByAggregateRootId("1"))
                .thenReturn(Flux.just(buyeruser,
                        walletfundedbuyer
                ));

        BDDMockito.when(this.domainEventRepository.findByAggregateRootId("2"))
                .thenReturn(Flux.just(selleruser,
                        walletfundedseller,
                        sellerbuycoin
                ));

        BDDMockito
                .when(this.domainEventRepository.saveDomainEvent((ArgumentMatchers.any(P2PTransactionCommitted.class))))
                .thenReturn(Mono.just(eventseller));

        BDDMockito
                .when(this.domainEventRepository.saveDomainEvent((ArgumentMatchers.any(P2PTransactionCommitted.class))))
                .thenReturn(Mono.just(eventbuyer));

        Mono<List<DomainEvent>> triggeredevents = this.useCase.apply(Mono.just(command))
                .collectList();

        StepVerifier.create(triggeredevents)
                .expectSubscription()
                .expectNextMatches(domainEvents ->

                        domainEvents instanceof P2PTransactionCommitted)
                .verifyComplete();

        //    BDDMockito.verify(this.eventBus, BDDMockito.times(1))
        //            .saveDomainEvent(ArgumentMatchers.any(DomainEvent.class));

        BDDMockito.verify(this.domainEventRepository, BDDMockito.times(2))
                .saveDomainEvent(ArgumentMatchers.any(DomainEvent.class));
    }
}