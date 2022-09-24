package co.com.sofkoin.alpha.infrastructure.web.middlewares;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class IllegalArgumentExceptionHandler {

  @ExceptionHandler({ IllegalArgumentException.class })
  public ResponseEntity<Object> illegalArgumentExceptionHandler(IllegalArgumentException exception) {
    HashMap<String, Object> body = new HashMap<>();
    body.put("errorMessage", exception.getMessage());

    return new ResponseEntity<>(
      body,
      HttpStatus.BAD_REQUEST
    );
  }
}
