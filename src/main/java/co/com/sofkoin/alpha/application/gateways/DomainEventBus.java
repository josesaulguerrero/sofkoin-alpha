package co.com.sofkoin.alpha.application.gateways;

import co.com.sofka.domain.generic.DomainEvent;

public interface DomainEventBus {
    void publishEvent(DomainEvent event);
}
