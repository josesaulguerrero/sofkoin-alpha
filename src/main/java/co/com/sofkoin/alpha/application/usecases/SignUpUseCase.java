package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.application.gateways.PasswordEncoder;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.user.commands.SignUp;
import co.com.sofkoin.alpha.domain.user.entities.root.User;
import co.com.sofkoin.alpha.domain.user.values.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Locale;

@Service
@AllArgsConstructor
public class SignUpUseCase implements UseCase<SignUp> {
    private final DomainEventRepository domainEventRepository;
    private final DomainEventBus domainEventBus;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Flux<DomainEvent> apply(Mono<SignUp> command) {
        return command
                .flatMap(next -> {
                    Flux<DomainEvent> events = this.domainEventRepository.findUserDomainEventsByEmail(next.getEmail());
                    return events.hasElements()
                            .map(containsElements -> {
                                if (containsElements) {
                                    throw new IllegalArgumentException("You cannot register more than one user with the same email.");
                                }
                                return next;
                            });
                })
                .map(c -> {
                    Password password = new Password(c.getPassword());
                    Password encryptedPassword = new Password(this.passwordEncoder.encode(password.value()));

                    return new User(
                            new UserID(),
                            new FullName(c.getName(), c.getSurname()),
                            encryptedPassword,
                            new Email(c.getEmail()),
                            new Phone(c.getPhoneNumber()),
                            new Avatar(c.getAvatarUrl()),
                            AuthMethod.valueOf(c.getAuthMethod().toUpperCase(Locale.ROOT).trim())
                    );
                })
                .flatMapIterable(AggregateEvent::getUncommittedChanges)
                .flatMap(event ->
                        this.domainEventRepository
                                .saveDomainEvent(event)
                                .doOnNext(this.domainEventBus::publishEvent)
                );
    }
}
