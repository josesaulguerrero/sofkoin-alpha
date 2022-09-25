package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.market.commands.PublishP2POffer;
import co.com.sofkoin.alpha.domain.market.events.MarketCreated;
import co.com.sofkoin.alpha.domain.market.events.P2POfferPublished;
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
        MarketCreated market = new MarketCreated("Colombia", "23812");
        market.setAggregateRootId("5498vfcd");
        PublishP2POffer offer = new PublishP2POffer(
                "5498vfcd",
                "edfanon303",
                "11KM11T",
                "XRP",
                57.4,
                50.5);

        P2POfferPublished offerEvent = new P2POfferPublished(
                "151fdea",
                "kat11Kmi",
                "edfanon303",
                "XRP",
                57.4,
                50.5,
                "1fsvibreuiw");
        offerEvent.setAggregateParentId("5498vfcd");


        Mockito.when(domainEventRepository.findByAggregateRootId(Mockito.any(String.class))).thenReturn(Flux.just(market));
        Mockito.when(domainEventRepository.saveDomainEvent(Mockito.any(P2POfferPublished.class))).thenReturn(Mono.just(offerEvent));

        var use = (Flux<DomainEvent>)publishP2POfferUseCase.apply(Mono.just(offer));

        StepVerifier.create(use)
                .expectNext(offerEvent)
                .verifyComplete();

        Mockito.verify(domainEventRepository,Mockito.atLeastOnce()).findByAggregateRootId(Mockito.any(String.class));
        Mockito.verify(domainEventRepository, Mockito.atLeastOnce()).saveDomainEvent(Mockito.any(P2POfferPublished.class));
        Mockito.verify(domainEventBus, Mockito.atLeastOnce()).publishEvent(Mockito.any(P2POfferPublished.class));
    }
}