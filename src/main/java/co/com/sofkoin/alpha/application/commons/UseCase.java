package co.com.sofkoin.alpha.application.commons;

import co.com.sofka.domain.generic.Command;
import co.com.sofka.domain.generic.DomainEvent;
import reactor.core.CorePublisher;

import java.util.function.Function;

public interface UseCase<T extends Command, K extends CorePublisher<? extends DomainEvent>> extends Function<T, K> {
    K apply(T command);
}
