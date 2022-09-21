package co.com.sofkoin.alpha.application.gateways;

import co.com.sofka.domain.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DomainEventRepository {
    Flux<? extends DomainEvent> findByAggregateRootId(String id);
    Mono<? extends DomainEvent> saveDomainEvent(DomainEvent event);
}
