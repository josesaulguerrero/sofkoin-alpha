package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.market.commands.CreateMarket;
import co.com.sofkoin.alpha.domain.market.events.MarketCreated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CreateMarketUseCaseTest {
    @Mock
    DomainEventRepository domainEventRepository;

    @Mock
    DomainEventBus domainEventBus;

    CreateMarketUseCase createMarketUseCase;

    @BeforeEach
    void init(){
        this.createMarketUseCase = new CreateMarketUseCase(domainEventBus,domainEventRepository);
    }

    @Test
    void createMarketUseCaseTest(){
        CreateMarket command = new CreateMarket("Colombia");
        MarketCreated event = new MarketCreated("Colombia" , "12478KL11");
        event.setAggregateRootId("415641");

        Mockito.when(domainEventRepository.saveDomainEvent(Mockito.any(MarketCreated.class)))
                .thenReturn(Mono.just(event));

        var use = (Flux<DomainEvent>)createMarketUseCase.apply(Mono.just(command));

        StepVerifier.create(use)
                .expectNextMatches(eventResult -> eventResult instanceof MarketCreated)
                .verifyComplete();

        Mockito.verify(domainEventRepository,Mockito.atLeastOnce()).saveDomainEvent(Mockito.any(MarketCreated.class));
        Mockito.verify(domainEventBus, Mockito.atLeastOnce()).publishEvent(Mockito.any(MarketCreated.class));
    }
}