package co.com.sofkoin.alpha.application.commons;

import co.com.sofka.domain.generic.Command;
import co.com.sofka.domain.generic.DomainEvent;
import reactor.core.CorePublisher;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface UseCase<T extends Command> extends Function<Mono<T>, CorePublisher<? extends DomainEvent>> {
    CorePublisher<? extends DomainEvent> apply(Mono<T> command);
}
