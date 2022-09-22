package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.user.commands.LogIn;
import co.com.sofkoin.alpha.domain.user.events.UserLoggedIn;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import co.com.sofkoin.alpha.domain.user.values.RegisterMethod;
import co.com.sofkoin.alpha.infrastructure.config.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LogInUseCaseTest {

  @Mock
  private DomainEventRepository domainEventRepository;
  @Mock
  private DomainEventBus domainEventBus;
  @Mock
  private JwtTokenProvider tokenProvider;
  @Mock
  private ReactiveAuthenticationManager authenticationManager;
  @InjectMocks
  private LogInUseCase useCase;

  @Test
  void logInUseCaseTest() {
    final String JWT_TOKEN = "7373277213";

    LogIn command = new LogIn(
            "2838128321",
            "someone@gmail.com",
            "my_strong_PASSWORD_12345",
            RegisterMethod.MANUAL.name()
    );

    UserSignedUp evSignedUp = new UserSignedUp(
            command.getUserId(),
            command.getEmail(),
            command.getPassword(),
            "David",
            "Rueda",
            "2381732714",
            "http://somewhere.com",
            command.getLoginMethod()
    );

    UserLoggedIn evLogIn = new UserLoggedIn(
            command.getUserId(),
            command.getEmail(),
            command.getPassword(),
            command.getLoginMethod(),
            JWT_TOKEN
    );

    var user = new UsernamePasswordAuthenticationToken("", "");

    BDDMockito
      .when(domainEventRepository.saveDomainEvent(BDDMockito.any(DomainEvent.class)))
      .thenReturn(Mono.just(evLogIn));

    BDDMockito
      .when(domainEventRepository.findByAggregateRootId(BDDMockito.anyString()))
      .thenReturn(Flux.just(evSignedUp));

    BDDMockito
      .when(tokenProvider.createJwtToken(BDDMockito.any(Authentication.class)))
      .thenReturn(JWT_TOKEN);

    BDDMockito
      .when(authenticationManager.authenticate(BDDMockito.any(Authentication.class)))
      .thenReturn(Mono.just(user));


    Mono<List<DomainEvent>> logIn = useCase.apply(Mono.just(command))
            .collectList();


    StepVerifier.create(logIn)
            .expectSubscription()
            .assertNext(evs -> {
              UserLoggedIn ev = (UserLoggedIn) evs.get(0);
              System.out.println(ev);

              assertEquals(1, evs.size());
              assertEquals(evLogIn.getJwt(), ev.getJwt());
              assertEquals(evLogIn.getUserId(), ev.getUserId());
            })
            .expectComplete()
            .log()
            .verify();


    BDDMockito
      .verify(domainEventRepository, BDDMockito.times(1))
      .saveDomainEvent(BDDMockito.any(DomainEvent.class));
    BDDMockito
      .verify(domainEventRepository, BDDMockito.times(1))
      .findByAggregateRootId(BDDMockito.anyString());
    BDDMockito
      .verify(domainEventBus, BDDMockito.times(1))
      .publishEvent(BDDMockito.any(DomainEvent.class));
    BDDMockito
      .verify(authenticationManager, BDDMockito.times(1))
      .authenticate(BDDMockito.any(Authentication.class));
    BDDMockito
      .verify(tokenProvider, BDDMockito.times(1))
      .createJwtToken(BDDMockito.any(Authentication.class));
  }

}