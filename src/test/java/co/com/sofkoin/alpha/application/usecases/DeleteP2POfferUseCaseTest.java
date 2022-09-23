package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.market.commands.DeleteP2POffer;
import co.com.sofkoin.alpha.domain.market.events.MarketCreated;
import co.com.sofkoin.alpha.domain.market.events.P2POfferDeleted;
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

@ExtendWith(MockitoExtension.class)
class DeleteP2POfferUseCaseTest {
    @Mock
    DomainEventRepository domainEventRepository;

    @Mock
    DomainEventBus domainEventBus;

    DeleteP2POfferUseCase deleteP2POfferUseCase;

    @BeforeEach
    void init(){
        this.deleteP2POfferUseCase = new DeleteP2POfferUseCase(domainEventBus,domainEventRepository);
    }

    @Test
    void deleteP2POfferUseCaseTest(){
        MarketCreated market = new MarketCreated("Colombia");


        P2POfferPublished offerEvent = new P2POfferPublished(
                "151fdea",
                "Tl11K3",
                "edfanon303",
                "XRP",
                57.4,
                50.5,
                "1fsvibreuiw");


        DeleteP2POffer command = new DeleteP2POffer("5498vfcd", "151fdea");

        P2POfferDeleted event = new P2POfferDeleted("151fdea", "11Kt13");

        Mockito.when(domainEventRepository.findByAggregateRootId(Mockito.any(String.class))).thenReturn(Flux.just(market,offerEvent));
        Mockito.when(domainEventRepository.saveDomainEvent(Mockito.any(P2POfferDeleted.class))).thenReturn(Mono.just(event));

        var use = (Flux<DomainEvent>)deleteP2POfferUseCase.apply(Mono.just(command));

        StepVerifier.create(use)
                .expectNext(event)
                .verifyComplete();

        Mockito.verify(domainEventRepository, Mockito.atLeastOnce()).findByAggregateRootId(Mockito.any(String.class));
        Mockito.verify(domainEventRepository, Mockito.atLeastOnce()).saveDomainEvent(Mockito.any(P2POfferDeleted.class));
        Mockito.verify(domainEventBus, Mockito.atLeastOnce()).publishEvent(Mockito.any(P2POfferDeleted.class));

    }



}