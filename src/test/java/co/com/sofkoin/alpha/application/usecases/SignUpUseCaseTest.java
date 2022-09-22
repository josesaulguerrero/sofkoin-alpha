package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.application.gateways.PasswordEncoder;
import co.com.sofkoin.alpha.domain.user.commands.SignUp;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import co.com.sofkoin.alpha.domain.user.values.AuthMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SignUpUseCaseTest {
    @Mock
    private DomainEventRepository domainEventRepository;

    @Mock
    private DomainEventBus domainEventBus;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignUpUseCase useCase;

    @Test
    @DisplayName("#apply should create a new User when successful.")
    void apply_ShouldCreateANewUser_WhenSuccessful() {
        // Arrange
        SignUp command = new SignUp(
                "someone@gmail.com",
                "my_strong_PASSWORD_12345",
                "Pepito",
                "Suarez",
                "0980980998",
                "http://somewhere.com",
                AuthMethod.MANUAL.name()
        );
        UserSignedUp event = new UserSignedUp(
                UUID.randomUUID().toString(),
                command.getEmail(),
                command.getPassword(),
                command.getName(),
                command.getSurname(),
                command.getPhoneNumber(),
                command.getAvatarUrl(),
                command.getAuthMethod()
        );
        BDDMockito
                .when(this.domainEventRepository.saveDomainEvent(ArgumentMatchers.any(DomainEvent.class)))
                .thenReturn(Mono.just(event));
        BDDMockito
                .when(this.passwordEncoder.encode(ArgumentMatchers.anyString()))
                .thenReturn(command.getPassword());

        // Act
        Flux<DomainEvent> response = this.useCase.apply(Mono.just(command));

        // Assert
        StepVerifier.create(response)
                .expectSubscription()
                .expectNextMatches(d -> d instanceof UserSignedUp)
                .verifyComplete();

        BDDMockito
                .verify(this.domainEventRepository, BDDMockito.times(1))
                .saveDomainEvent(ArgumentMatchers.any(DomainEvent.class));
        BDDMockito
                .verify(this.domainEventBus, BDDMockito.times(1))
                .publishEvent(ArgumentMatchers.any(DomainEvent.class));
        BDDMockito
                .verify(this.passwordEncoder, BDDMockito.times(1))
                .encode(ArgumentMatchers.anyString());
    }
}