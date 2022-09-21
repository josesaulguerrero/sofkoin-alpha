package co.com.sofkoin.alpha.application.commons;

import co.com.sofka.domain.generic.Command;
import co.com.sofka.domain.generic.DomainEvent;
import reactor.core.CorePublisher;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface UseCase<T extends Command, K extends DomainEvent> extends Function<Mono<T>, CorePublisher<K>> {
    CorePublisher<K> apply(Mono<T> command);
}
