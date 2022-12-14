package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.user.commands.CommitTradeTransaction;
import co.com.sofkoin.alpha.domain.user.events.TradeTransactionCommitted;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import co.com.sofkoin.alpha.domain.user.events.WalletFunded;
import co.com.sofkoin.alpha.domain.user.values.Timestamp;
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

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TradeTransactionUseCaseTest {

    @Mock
    private DomainEventBus eventBus;
    @Mock
    private DomainEventRepository domainEventRepository;

    @InjectMocks
    private TradeTransactionUseCase useCase;

    @Test
    void tradeTransactionUseCase() {

        CommitTradeTransaction command = new CommitTradeTransaction(
                "1",
                "BUY",
                "ETH",
                1.0,
                5.0
        );

        TradeTransactionCommitted event = new TradeTransactionCommitted(
                "33333",
                command.getBuyerId(),
                "BUY",
                command.getCryptoSymbol(),
                command.getCryptoAmount(),
                command.getCryptoAmount(),
                command.getCryptoAmount() * command.getCryptoPrice(),
                new Timestamp().toString()
        );
        var user = new UserSignedUp(
                "1",
                "maxmusterman@daad.de",
                "maxmusterman@daad.de",
                "Max",
                "Musterman",
                "0123456789",
                "http://www.acatar.url.com",
                "GMAIL"
        );

        var walletFunded = new WalletFunded(
                "1",
                100.0,
                new Timestamp().toString()
        );

        BDDMockito.when(this.domainEventRepository.findByAggregateRootId(BDDMockito.anyString()))
                .thenReturn(Flux.just(user, walletFunded));

        BDDMockito
                .when(this.domainEventRepository.saveDomainEvent(ArgumentMatchers.any(DomainEvent.class)))
                .thenReturn(Mono.just(event));


        Mono<List<DomainEvent>> triggeredEvents = this.useCase.apply(Mono.just(command))
                .collectList();

        StepVerifier.create(triggeredEvents)
                .expectSubscription()
                .expectNextMatches(domainEvents ->
                        domainEvents.size() == 1 && domainEvents.get(0) instanceof TradeTransactionCommitted
                )
                .verifyComplete();

        BDDMockito.verify(this.domainEventRepository, BDDMockito.times(1))
                .saveDomainEvent(ArgumentMatchers.any(DomainEvent.class));

        BDDMockito.verify(this.eventBus, BDDMockito.times(1))
                .publishEvent(ArgumentMatchers.any(DomainEvent.class));

    }
}