package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.user.commands.FundWallet;
import co.com.sofkoin.alpha.domain.user.commands.LogOut;
import co.com.sofkoin.alpha.domain.user.events.UserLoggedOut;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import co.com.sofkoin.alpha.domain.user.events.WalletFunded;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LogOutUseCaseTest {

    @Mock
    DomainEventRepository repository;

    @Mock
    DomainEventBus bus;

    @Mock
    LogOutUseCase useCase;

    @BeforeEach
    void init() {
        useCase = new LogOutUseCase(repository,bus);
    }

    @Test
    @DisplayName("FundWalletUseCaseTest")
    void fundWalletUseCaseTest(){
        var newUser = new UserSignedUp(
                "1",
                "email@test.com",
                "email@test.com",
                "testName",
                "testSurname",
                "1234567890",
                "http://www.avatarurl.com",
                "GMAIL"
        );
        newUser.setAggregateRootId("1");

        var userLoggedOut = new UserLoggedOut(newUser.getUserId());
        userLoggedOut.setAggregateRootId("1");

        var command = new LogOut(newUser.getUserId());

        Mono<DomainEvent> responseExpected = Mono.just(userLoggedOut);

        BDDMockito.when(repository.findByAggregateRootId(BDDMockito.anyString()))
                .thenReturn(Flux.just(newUser));

        BDDMockito.when(repository.saveDomainEvent(BDDMockito.any(DomainEvent.class)))
                .thenReturn(responseExpected);

        var useCaseExecute = useCase.apply(Mono.just(command)).collectList();

        StepVerifier.create(useCaseExecute)
                .expectNextMatches(events ->
                        events.get(0).aggregateRootId().equals("1") &&
                                events.get(0) instanceof UserLoggedOut)
                .expectComplete().verify();

        BDDMockito.verify(repository).findByAggregateRootId(BDDMockito.anyString());
        BDDMockito.verify(repository).saveDomainEvent(BDDMockito.any(DomainEvent.class));

    }

}