package co.com.sofkoin.alpha.application.gateways;

import co.com.sofka.domain.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DomainEventRepository {
    Flux<DomainEvent> findByAggregateRootId(String id);
    Flux<DomainEvent> findUserDomainEventsByEmail(String email);
    Mono<DomainEvent> saveDomainEvent(DomainEvent event);
}
