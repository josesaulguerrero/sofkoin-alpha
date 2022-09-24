package co.com.sofkoin.alpha.infrastructure.web.controllers;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.usecases.SignUpUseCase;
import co.com.sofkoin.alpha.domain.user.commands.LogIn;
import co.com.sofkoin.alpha.domain.user.commands.SignUp;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {
    private SignUpUseCase signUpUseCase;
    // private LogInUseCase loginUseCase;

    @PostMapping("signup")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Flux<DomainEvent>> signUp(@RequestBody @Valid SignUp body) {
        return new ResponseEntity<>(
                this.signUpUseCase.apply(Mono.just(body)),
                HttpStatus.CREATED
        );
    }

//    @PostMapping("login")
//    @PreAuthorize("permitAll()")
//    public ResponseEntity<Flux<DomainEvent>> logIn(@RequestBody @Valid LogIn body) {
//        return new ResponseEntity<>(
//                this.logInUseCase.apply(Mono.just(body)),
//                HttpStatus.OK
//        );
//    }
}
