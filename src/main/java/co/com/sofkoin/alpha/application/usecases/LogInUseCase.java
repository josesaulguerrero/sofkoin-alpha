package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.user.commands.LogIn;
import co.com.sofkoin.alpha.domain.user.entities.root.User;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import co.com.sofkoin.alpha.domain.user.values.Email;
import co.com.sofkoin.alpha.domain.user.values.Password;
import co.com.sofkoin.alpha.domain.user.values.AuthMethod;
import co.com.sofkoin.alpha.infrastructure.config.security.jwt.JWTProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Service
@AllArgsConstructor
public class LogInUseCase implements UseCase<LogIn> {

    private final DomainEventRepository domainEventRepository;
    private final DomainEventBus domainEventBus;
    private final JWTProvider tokenProvider;
    private final ReactiveAuthenticationManager authenticationManager;

    @Override
    public Flux<DomainEvent> apply(Mono<LogIn> command) {
        return
                command
                        .flatMap(com ->
                                authenticationManager
                                        .authenticate(new UsernamePasswordAuthenticationToken(com.getEmail(), com.getPassword()))
                                        .onErrorMap(BadCredentialsException.class, err -> new Throwable(HttpStatus.FORBIDDEN.toString()))
                                        .map(tokenProvider::createJwtToken)
                                        .flatMap(token ->
                                                domainEventRepository
                                                        .findUserDomainEventsByEmail(com.getEmail())
                                                        .collectList()
                                                        .map(events -> {
                                                            User user = User.from(
                                                                    new UserID(((UserSignedUp) events.get(0)).getUserId()),
                                                                    events
                                                            );
                                                            user.logIn(
                                                                    new UserID(user.identity().value()),
                                                                    new Email(com.getEmail()),
                                                                    new Password(com.getPassword()),
                                                                    AuthMethod.valueOf(com.getAuthMethod().toUpperCase(Locale.ROOT).trim()),
                                                                    token
                                                            );
                                                            return user;
                                                        })))
                        .flatMapIterable(AggregateEvent::getUncommittedChanges)
                        .flatMap(domainEventRepository::saveDomainEvent)
                        .doOnNext(domainEventBus::publishEvent);
    }
}
