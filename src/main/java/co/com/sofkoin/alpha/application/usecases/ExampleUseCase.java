package co.com.sofkoin.alpha.application.usecases;

import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.domain.user.commands.SignUp;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ExampleUseCase implements UseCase<SignUp, UserSignedUp> {
    @Override
    public Flux<UserSignedUp> apply(Mono<SignUp> command) {
        return null;
    }
}
