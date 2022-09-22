package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.user.commands.SaveOfferMessage;
import co.com.sofkoin.alpha.domain.user.events.OfferMessageSaved;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import co.com.sofkoin.alpha.domain.user.values.RegisterMethod;
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

        var command = new SaveOfferMessage("1", "2",
                "BTC", 0.11, 19117.11);

        var domainEvent = new OfferMessageSaved("17K11", command.getSenderId(),
                command.getReceiverId(), command.getCryptoSymbol(),
                command.getCryptoAmount(), command.getCryptoPrice());

        var receiverSignedUp = new UserSignedUp(command.getReceiverId(),
                "stephany@email.com",
                "Test123ABC",
                "Stephany",
                "Yepes",
                "3108509630",
                "https://www.freepik.es/psd-gratis/ilustracion-27470311",
                RegisterMethod.MANUAL.name()
        );

        var senderSignedUp = new UserSignedUp(command.getSenderId(),
                "katerin@email.com",
                "Test789ABC",
                "Katerin",
                "Calderón",
                "3108512397",
                "https://www.freepik.es/psd-gratis/ilustracion-27470375",
                RegisterMethod.MANUAL.name()
        );

        BDDMockito.when(repositoryMock.findByAggregateRootId(command.getReceiverId()))
                .thenReturn(Flux.just(receiverSignedUp));

        BDDMockito.when(repositoryMock.findByAggregateRootId(command.getSenderId()))
                .thenReturn(Flux.just(senderSignedUp));

        BDDMockito.when(repositoryMock.saveDomainEvent(Mockito.any(DomainEvent.class)))
                .thenReturn(Mono.just(domainEvent));

        var useCase = saveOfferMessageUseCase.apply(Mono.just(command));

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