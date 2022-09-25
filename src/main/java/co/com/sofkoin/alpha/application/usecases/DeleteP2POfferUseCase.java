package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.market.commands.DeleteP2POffer;
import co.com.sofkoin.alpha.domain.market.entities.root.Market;
import co.com.sofkoin.alpha.domain.market.values.identities.MarketID;
import co.com.sofkoin.alpha.domain.market.values.identities.OfferId;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@AllArgsConstructor
@Component
public class DeleteP2POfferUseCase implements UseCase<DeleteP2POffer> {
    DomainEventBus domainEventBus;

    DomainEventRepository domainEventRepository;

    @Override
    public Flux<DomainEvent> apply(Mono<DeleteP2POffer> deleteP2POfferCommand) {
        return deleteP2POfferCommand.flatMapMany(command -> domainEventRepository.findByAggregateRootId(command.getMarketId())
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("The market with the given Id does not exist.")))
                        .collectList()
                        .flatMapIterable(events -> {
                            Market market = Market.from(new MarketID(command.getMarketId()), events);

                            OfferId offerId = new OfferId(command.getOfferId());
                            var offer = market.findOfferById(offerId.value());

                            market.deleteP2POffer(offer.identity());
                            log.info("DeleteP2POfferUseCase deleted successfully");

                            return market.getUncommittedChanges();
                        })
                        .flatMap(event -> domainEventRepository.saveDomainEvent(event)))
                .doOnNext(event -> domainEventBus.publishEvent(event));
    }
}
