package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.user.commands.SaveOfferMessage;
import co.com.sofkoin.alpha.domain.user.events.OfferMessageSaved;
import co.com.sofkoin.alpha.domain.user.events.TradeTransactionCommitted;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import co.com.sofkoin.alpha.domain.user.events.WalletFunded;
import co.com.sofkoin.alpha.domain.user.values.AuthMethod;
import co.com.sofkoin.alpha.domain.user.values.MessageRelationTypes;
import co.com.sofkoin.alpha.domain.user.values.Timestamp;
import co.com.sofkoin.alpha.domain.user.values.TransactionTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SaveOfferMessageUseCaseTest {

    @Mock
    DomainEventRepository repositoryMock;

    @Mock
    DomainEventBus eventBus;

    @InjectMocks
    SaveOfferMessageUseCase saveOfferMessageUseCase;

    @Test
    void saveOfferMessageUseCaseTest(){

        var saveOfferMessage = new SaveOfferMessage(
                "31232",
                "1",
                "2",
                "BTC",
                0.11,
                19117.11
        );

        var offerMessageSaved = new OfferMessageSaved(
                UUID.randomUUID().toString(),
                saveOfferMessage.getMarketId(),
                saveOfferMessage.getSenderId(),
                saveOfferMessage.getReceiverId(),
                saveOfferMessage.getCryptoSymbol(),
                MessageRelationTypes.RECEIVER.name(),
                saveOfferMessage.getCryptoAmount(),
                saveOfferMessage.getCryptoPrice()
        );

        var receiverSignedUp = new UserSignedUp(saveOfferMessage.getReceiverId(),
                "stephany@email.com",
                "Test123ABC",
                "Stephany",
                "Yepes",
                "3108509630",
                "https://www.freepik.es/psd-gratis/ilustracion-27470311",
                AuthMethod.MANUAL.name()
        );

        WalletFunded walletFunded = new WalletFunded(
                receiverSignedUp.getUserId(),
                100000.0,
                new Timestamp().toString()
        );

        TradeTransactionCommitted tradeTransactionCommitted = new TradeTransactionCommitted(
                UUID.randomUUID().toString(),
                receiverSignedUp.getUserId(),
                TransactionTypes.BUY.name(),
                "BTC",
                3.0,
                15000.0,
                45000.0,
                new Timestamp().toString()
        );

        var senderSignedUp = new UserSignedUp(saveOfferMessage.getSenderId(),
                "katerin@email.com",
                "Test789ABC",
                "Katerin",
                "Calderón",
                "3108512397",
                "https://www.freepik.es/psd-gratis/ilustracion-27470375",
                AuthMethod.MANUAL.name()
        );

        BDDMockito.when(repositoryMock.findByAggregateRootId(saveOfferMessage.getReceiverId()))
                .thenReturn(Flux.just(receiverSignedUp, walletFunded, tradeTransactionCommitted));

        BDDMockito.when(repositoryMock.findByAggregateRootId(saveOfferMessage.getSenderId()))
                .thenReturn(Flux.just(senderSignedUp));

        BDDMockito.when(repositoryMock.saveDomainEvent(Mockito.any(DomainEvent.class)))
                .thenReturn(Mono.just(offerMessageSaved));

        var useCase = saveOfferMessageUseCase.apply(Mono.just(saveOfferMessage));

        StepVerifier.create(useCase)
                .expectSubscription()
                .expectNextMatches(events -> events instanceof OfferMessageSaved)
                .expectNextMatches(events -> events instanceof OfferMessageSaved)
                .verifyComplete();

        BDDMockito.verify(eventBus, BDDMockito.times(2))
                .publishEvent(Mockito.any(DomainEvent.class));

        BDDMockito.verify(repositoryMock, BDDMockito.times(2))
                .saveDomainEvent(Mockito.any(DomainEvent.class));

        BDDMockito.verify(repositoryMock, BDDMockito.times(2))
                .findByAggregateRootId(Mockito.any(String.class));

    }

}