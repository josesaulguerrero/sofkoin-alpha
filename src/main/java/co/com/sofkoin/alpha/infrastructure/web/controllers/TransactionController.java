package co.com.sofkoin.alpha.infrastructure.web.controllers;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.usecases.FundWalletUseCase;
import co.com.sofkoin.alpha.application.usecases.P2PTransactionUseCase;
import co.com.sofkoin.alpha.application.usecases.TradeTransactionUseCase;
import co.com.sofkoin.alpha.domain.user.commands.CommitP2PTransaction;
import co.com.sofkoin.alpha.domain.user.commands.CommitTradeTransaction;
import co.com.sofkoin.alpha.domain.user.commands.FundWallet;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("transaction")
public class TransactionController {

    P2PTransactionUseCase p2PTransactionUseCase;
    TradeTransactionUseCase tradeTransactionuseCase;
    FundWalletUseCase fundWalletUseCase;

    @PostMapping("p2p")
    public ResponseEntity<Flux<DomainEvent>> commitP2PTransaction(@RequestBody @Valid CommitP2PTransaction body) {
        return new ResponseEntity<>(
                this.p2PTransactionUseCase.apply(Mono.just(body)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("trade")
    public ResponseEntity<Flux<DomainEvent>> commitTradeTransaction(@RequestBody @Valid CommitTradeTransaction body) {
        return new ResponseEntity<>(
                this.tradeTransactionuseCase.apply(Mono.just(body)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("fund")
    public ResponseEntity<Flux<DomainEvent>> fundWallet(@RequestBody @Valid FundWallet body) {
        return new ResponseEntity<>(
                this.fundWalletUseCase.apply(Mono.just(body)),
                HttpStatus.CREATED
        );
    }

}
