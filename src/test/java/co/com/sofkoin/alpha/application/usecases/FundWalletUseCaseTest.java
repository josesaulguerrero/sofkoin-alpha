package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.user.commands.FundWallet;
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

@ExtendWith(MockitoExtension.class)
class FundWalletUseCaseTest {

    @Mock
    DomainEventRepository repository;

    @Mock
    FundWalletUseCase useCase;

    @Mock
    DomainEventBus bus;

    @BeforeEach
    void init(){
        useCase = new FundWalletUseCase(repository,bus);
    }

    @Test
    @DisplayName("FundWalletUseCaseTest")
    void fundWalletUseCaseTest(){
        var newUser = new UserSignedUp(
                "1",
                "email@test.com",
                "PasswordTest111",
                "testName",
                "testSurname",
                "1234567890",
                "http://www.avatarurl.com",
                "MANUAL"
        );
        newUser.setAggregateRootId("1");

        var walletFunded = new WalletFunded("1",1000000D, "09/21/2922 04:24:32");
        walletFunded.setAggregateRootId("1");

        var command = new FundWallet(walletFunded.getUserId(),walletFunded.getCashAmount());

        Mono<DomainEvent> responseExpected = Mono.just(walletFunded);

        BDDMockito.when(repository.findByAggregateRootId(BDDMockito.anyString()))
                .thenReturn(Flux.just(newUser));

        BDDMockito.when(repository.saveDomainEvent(BDDMockito.any(DomainEvent.class)))
                .thenReturn(responseExpected);

        var useCaseExecute = useCase.apply(Mono.just(command)).collectList();

        StepVerifier.create(useCaseExecute)
                .expectNextMatches(events ->
                        events.get(0).aggregateRootId().equals("1") &&
                                events.get(0) instanceof WalletFunded)
                .expectComplete().verify();

        BDDMockito.verify(repository).findByAggregateRootId(BDDMockito.anyString());
        BDDMockito.verify(repository).saveDomainEvent(BDDMockito.any(DomainEvent.class));
    }
}