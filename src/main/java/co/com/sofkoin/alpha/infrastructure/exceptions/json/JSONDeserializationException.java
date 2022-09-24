package co.com.sofkoin.alpha.infrastructure.exceptions.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class JSONDeserializationException extends RuntimeException {
    private final String cause;
}
