package co.com.sofkoin.alpha.domain.user.values;

import co.com.sofka.domain.generic.ValueObject;
import org.apache.commons.validator.GenericValidator;

public class Email implements ValueObject<String> {
    private final String value;

    public Email(String value) {
        if (!GenericValidator.isEmail(value)) {
            throw new IllegalArgumentException("Invalid email (Must follow this pattern: 'a@b.xyz').");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }
}
