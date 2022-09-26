package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.market.commands.CreateMarket;
import co.com.sofkoin.alpha.domain.market.commands.PublishP2POffer;
import co.com.sofkoin.alpha.domain.market.events.MarketCreated;
import co.com.sofkoin.alpha.domain.market.events.P2POfferPublished;
import co.com.sofkoin.alpha.domain.user.events.TradeTransactionCommitted;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import co.com.sofkoin.alpha.domain.user.events.WalletFunded;
import co.com.sofkoin.alpha.domain.user.values.Timestamp;
import co.com.sofkoin.alpha.domain.user.values.TransactionTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.management.MonitorInfo;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class PublishP2POfferUseCaseTest {
    @Mock
    DomainEventRepository domainEventRepository;

    @Mock
    DomainEventBus domainEventBus;

    PublishP2POfferUseCase publishP2POfferUseCase;

    @BeforeEach
    void init(){
        this.publishP2POfferUseCase = new PublishP2POfferUseCase(domainEventBus,domainEventRepository);
    }

    @Test
    void publishP2POfferUseCaseTest() {
        UserSignedUp userSignedUpEvent = new UserSignedUp(
                "1",
                "maxmusterman@daad.de",
                "maxmusterman@daad.de",
                "Max",
                "Musterman",
                "0123456789",
                "http://www.acatar.url.com",
                "GMAIL"
        );

        WalletFunded walletFunded = new WalletFunded(
                userSignedUpEvent.getUserId(),
                100.0,
                new Timestamp().toString()
        );

        TradeTransactionCommitted tradeTransactionCommittedEvent = new TradeTransactionCommitted(
                UUID.randomUUID().toString(),
                userSignedUpEvent.getUserId(),
                TransactionTypes.BUY.name(),
                "XRP",
                60.0,
                20.0,
                1200.0,
                new Timestamp().toString()
        );

        MarketCreated marketCreatedEvent = new MarketCreated(
                "Colombia",
                "23812"
        );

        PublishP2POffer createOffer = new PublishP2POffer(
                marketCreatedEvent.getMarketId(),
                userSignedUpEvent.getUserId(),
                "-",
                "XRP",
                57.4,
                50.5
        );

        P2POfferPublished offerEvent = new P2POfferPublished(
                UUID.randomUUID().toString(),
                createOffer.getMarketId(),
                createOffer.getPublisherId(),
                createOffer.getCryptoSymbol(),
                createOffer.getOfferCryptoAmount(),
                createOffer.getOfferCryptoPrice(),
                createOffer.getTargetAudienceId()
        );

        Mockito
                .when(domainEventRepository.findByAggregateRootId(createOffer.getMarketId()))
                .thenReturn(Flux.just(marketCreatedEvent));
        Mockito
                .when(domainEventRepository.findByAggregateRootId(createOffer.getPublisherId()))
                .thenReturn(Flux.just(userSignedUpEvent, walletFunded, tradeTransactionCommittedEvent));
        Mockito
                .when(domainEventRepository.saveDomainEvent(Mockito.any(P2POfferPublished.class)))
                .thenReturn(Mono.just(offerEvent));

        StepVerifier.create(publishP2POfferUseCase.apply(Mono.just(createOffer)))
                .expectNext(offerEvent)
                .verifyComplete();

        Mockito.verify(domainEventRepository,Mockito.atLeastOnce()).findByAggregateRootId(Mockito.any(String.class));
        Mockito.verify(domainEventRepository, Mockito.atLeastOnce()).saveDomainEvent(Mockito.any(P2POfferPublished.class));
        Mockito.verify(domainEventBus, Mockito.atLeastOnce()).publishEvent(Mockito.any(P2POfferPublished.class));
    }
}