package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.market.events.MarketCreated;
import co.com.sofkoin.alpha.domain.market.events.P2POfferPublished;
import co.com.sofkoin.alpha.domain.user.commands.CommitP2PTransaction;
import co.com.sofkoin.alpha.domain.user.events.P2PTransactionCommitted;
import co.com.sofkoin.alpha.domain.user.events.TradeTransactionCommitted;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import co.com.sofkoin.alpha.domain.user.events.WalletFunded;
import co.com.sofkoin.alpha.domain.user.values.Timestamp;
import co.com.sofkoin.alpha.domain.user.values.TransactionTypes;
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
    private DomainEventBus eventBus;
    @Mock
    private DomainEventRepository domainEventRepository;

    @Mock
    private DeleteP2POfferUseCase deleteP2POfferUseCase;

    @InjectMocks
    private P2PTransactionUseCase useCase;

    @Test
    void P2PTransactionUseCase() {
        //Command

        P2POfferPublished p2pOfferPublishEvent = new P2POfferPublished(
                "11k",
                "23812",
                "2",
                "ETH",
                1.0,
                1500.0,
                "-"
        );

        MarketCreated marketCreatedEvent = new MarketCreated(
                "Colombia",
                p2pOfferPublishEvent.getMarketId()
        );

        CommitP2PTransaction command = new CommitP2PTransaction(
                "1",
                p2pOfferPublishEvent.getMarketId(),
                p2pOfferPublishEvent.getOfferId()
        );

        //events
        P2PTransactionCommitted eventSeller = new P2PTransactionCommitted(
                "33333",
                p2pOfferPublishEvent.getPublisherId(),
                command.getBuyerId(),
                command.getOfferId(),
                command.getMarketId(),
                p2pOfferPublishEvent.getCryptoSymbol(),
                p2pOfferPublishEvent.getCryptoAmount(),
                p2pOfferPublishEvent.getCryptoPrice(),
                TransactionTypes.SELL.name(),
                p2pOfferPublishEvent.getCryptoAmount() * p2pOfferPublishEvent.getCryptoPrice(),
                new Timestamp().toString()
        );
        P2PTransactionCommitted eventBuyer = new P2PTransactionCommitted(
                "33333",
                p2pOfferPublishEvent.getPublisherId(),
                command.getBuyerId(),
                command.getOfferId(),
                command.getMarketId(),
                p2pOfferPublishEvent.getCryptoSymbol(),
                p2pOfferPublishEvent.getCryptoAmount(),
                p2pOfferPublishEvent.getCryptoPrice(),
                TransactionTypes.BUY.name(),
                p2pOfferPublishEvent.getCryptoAmount() * p2pOfferPublishEvent.getCryptoPrice(),
                new Timestamp().toString()
        );

        //usersignedupevents
        var buyerUserSignedUp = new UserSignedUp(
                "1",
                "maxmusterman@daad.de",
                "maxmusterman@daad.de",
                "Max",
                "Musterman",
                "0123456789",
                "http://www.acatar.url.com",
                "GMAIL"
        );
        var sellerUserSignedUp = new UserSignedUp(
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
        var walletFundedBuyer = new WalletFunded(
                "1",
                5000.0,
                new Timestamp().toString()
        );
        //add money to seller
        var walletFundedSeller = new WalletFunded(
                "2",
                100000.0,
                new Timestamp().toString()
        );
        //add crypto to seller
        var sellerBuyCoin = new TradeTransactionCommitted(
                "11",
                "2",
                "BUY",
                "ETH",
                4.0,
                1500.0,
                6000.0,
                new Timestamp().toString()
        );

        BDDMockito.when(this.domainEventRepository.findByAggregateRootId(marketCreatedEvent.getMarketId()))
                .thenReturn(Flux.just(marketCreatedEvent, p2pOfferPublishEvent));

        BDDMockito.when(this.domainEventRepository.findByAggregateRootId("1"))
                .thenReturn(Flux.just(
                        buyerUserSignedUp,
                        walletFundedBuyer
                ));


        BDDMockito.when(this.domainEventRepository.findByAggregateRootId("2"))
                .thenReturn(Flux.just(
                        sellerUserSignedUp,
                        walletFundedSeller,
                        sellerBuyCoin
                ));

        BDDMockito.when(this.deleteP2POfferUseCase.apply(ArgumentMatchers.any()))
                .thenReturn(Flux.empty());

        BDDMockito
                .when(this.domainEventRepository.saveDomainEvent((ArgumentMatchers.any(DomainEvent.class))))
                .then(i -> Mono.just(i.getArgument(0)));


        Flux<DomainEvent> triggeredEvents = this.useCase.apply(Mono.just(command));

        StepVerifier.create(triggeredEvents)
                .expectSubscription()
                .expectNextMatches(domainEvents -> domainEvents instanceof P2PTransactionCommitted)
                .expectNextMatches(domainEvents -> domainEvents instanceof P2PTransactionCommitted)
                .verifyComplete();

        BDDMockito.verify(this.domainEventRepository, BDDMockito.times(2))
                .saveDomainEvent(ArgumentMatchers.any(DomainEvent.class));

        BDDMockito.verify(this.eventBus, BDDMockito.times(2))
                .publishEvent(ArgumentMatchers.any(DomainEvent.class));

        BDDMockito.verify(this.deleteP2POfferUseCase, BDDMockito.times(1))
                .apply(ArgumentMatchers.any());

    }
}