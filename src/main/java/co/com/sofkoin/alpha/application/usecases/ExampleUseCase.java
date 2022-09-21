package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.domain.user.commands.SignUp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ExampleUseCase implements UseCase<SignUp> {
    @Override
    public Flux<DomainEvent> apply(Mono<SignUp> command) {
        return null;
    }
}
