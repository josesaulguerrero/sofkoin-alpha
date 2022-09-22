package co.com.sofkoin.alpha.infrastructure.web.controllers;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.usecases.ChangeMessageStatusUseCase;
import co.com.sofkoin.alpha.application.usecases.SaveOfferMessageUseCase;
import co.com.sofkoin.alpha.domain.user.commands.ChangeMessageStatus;
import co.com.sofkoin.alpha.domain.user.commands.SaveOfferMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("message")
public class MessageController {

    ChangeMessageStatusUseCase changeMessageStatusUseCase;
    SaveOfferMessageUseCase saveOfferMessageUseCase;

    @PostMapping("save")
    public ResponseEntity<Flux<DomainEvent>> saveOfferMessage(@RequestBody @Valid SaveOfferMessage body) {
        return new ResponseEntity<>(
                this.saveOfferMessageUseCase.apply(Mono.just(body)),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("update/status")
    public ResponseEntity<Flux<DomainEvent>> updateMessageStatus(@RequestBody @Valid ChangeMessageStatus body) {
        return new ResponseEntity<>(
                this.changeMessageStatusUseCase.apply(Mono.just(body)),
                HttpStatus.OK
        );
    }

}
