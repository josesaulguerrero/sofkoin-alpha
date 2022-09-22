package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.market.commands.CreateMarket;
import co.com.sofkoin.alpha.domain.market.entities.root.Market;
import co.com.sofkoin.alpha.domain.market.values.Country;
import co.com.sofkoin.alpha.domain.market.values.identities.MarketID;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Component
@Log4j2

@AllArgsConstructor
public class CreateMarketUseCase implements UseCase<CreateMarket> {

    DomainEventBus domainEventBus;

    DomainEventRepository domainEventRepository;



    @Override
    public CorePublisher<? extends DomainEvent> apply(Mono<CreateMarket> createMarketCommand) {
        return createMarketCommand.flatMapIterable(command -> {
            Market market = new Market(new MarketID(command.getMarketId()), new Country(command.getCountry()));
            return market.getUncommittedChanges();
        }).flatMap(event -> {
            log.info("CreateMarketUseCase working");
            return domainEventRepository.saveDomainEvent(event).thenReturn(event);
        }).doOnNext(event -> domainEventBus.publishEvent(event));
    }
}
