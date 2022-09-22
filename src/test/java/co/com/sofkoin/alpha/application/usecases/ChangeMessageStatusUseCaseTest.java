package co.com.sofkoin.alpha.application.usecases;


import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.user.commands.ChangeMessageStatus;
import co.com.sofkoin.alpha.domain.user.events.MessageStatusChanged;
import co.com.sofkoin.alpha.domain.user.events.OfferMessageSaved;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import co.com.sofkoin.alpha.domain.user.values.MessageStatus;
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
class ChangeMessageStatusUseCaseTest {

    @Mock
    DomainEventRepository repositoryMock;

    @Mock
    DomainEventBus eventBus;

    @InjectMocks
    ChangeMessageStatusUseCase changeMessageStatusUseCase;

    @Test
    void changeMessageStatusUseCaseTest() {

        var messageSaved = new OfferMessageSaved("175K", "2", "1", "BTC",
                0.05,19500.05);

        var command = new ChangeMessageStatus("1", "2",
                messageSaved.getMessageId(), MessageStatus.ACCEPTED.name());

        var domainEvent = new MessageStatusChanged(command.getReceiverId(), command.getSenderId(),
                command.getMessageId(), command.getNewStatus());

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
                .thenReturn(Flux.just(receiverSignedUp, messageSaved));

        BDDMockito.when(repositoryMock.findByAggregateRootId(command.getSenderId()))
                .thenReturn(Flux.just(senderSignedUp, messageSaved));

        BDDMockito.when(repositoryMock.saveDomainEvent(Mockito.any(DomainEvent.class)))
                .thenReturn(Mono.just(domainEvent));

        var useCase = changeMessageStatusUseCase.apply(Mono.just(command));

        StepVerifier.create(useCase)
                .expectSubscription()
                .expectNextMatches(events -> events instanceof MessageStatusChanged)
                .verifyComplete();

        BDDMockito.verify(eventBus, BDDMockito.times(2))
                .publishEvent(Mockito.any(DomainEvent.class));

        BDDMockito.verify(repositoryMock, BDDMockito.times(2))
                .saveDomainEvent(Mockito.any(DomainEvent.class));

        BDDMockito.verify(repositoryMock, BDDMockito.times(2))
                .findByAggregateRootId(Mockito.any(String.class));

    }

}