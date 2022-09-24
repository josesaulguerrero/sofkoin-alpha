package co.com.sofkoin.alpha.infrastructure.web.controllers;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.usecases.CreateMarketUseCase;
import co.com.sofkoin.alpha.application.usecases.DeleteP2POfferUseCase;
import co.com.sofkoin.alpha.application.usecases.PublishP2POfferUseCase;
import co.com.sofkoin.alpha.domain.market.commands.CreateMarket;
import co.com.sofkoin.alpha.domain.market.commands.DeleteP2POffer;
import co.com.sofkoin.alpha.domain.market.commands.PublishP2POffer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("market")
public class MarketController {

    CreateMarketUseCase createMarketUseCase;
    PublishP2POfferUseCase publishP2POfferUseCase;
    DeleteP2POfferUseCase deleteP2POfferUseCase;

    @PostMapping("create")
    public ResponseEntity<Flux<DomainEvent>> createMarket(@RequestBody @Valid CreateMarket body) {
        return new ResponseEntity<>(
                this.createMarketUseCase.apply(Mono.just(body)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("publish/offer")
    public ResponseEntity<Flux<DomainEvent>> publishP2POffer(@RequestBody @Valid PublishP2POffer body) {
        return new ResponseEntity<>(
                this.publishP2POfferUseCase.apply(Mono.just(body)),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("delete/offer")
    public ResponseEntity<Flux<DomainEvent>> deleteP2POffer(@RequestBody @Valid DeleteP2POffer body) {
        return new ResponseEntity<>(
                this.deleteP2POfferUseCase.apply(Mono.just(body)),
                HttpStatus.OK
        );
    }

}
