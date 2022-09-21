package co.com.sofkoin.alpha.domain.user.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.validator.GenericValidator;

@EqualsAndHashCode
@ToString
public class Phone implements ValueObject<String> {
    private final String value;

    public Phone(String value) {
        String PHONE_REGEX = "^\\d{10}$";
        if (!GenericValidator.matchRegexp(value, PHONE_REGEX)) {
            throw new IllegalArgumentException("Invalid Phone (Must contain exactly 10 numbers).");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }
}
