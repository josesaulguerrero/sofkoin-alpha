package co.com.sofkoin.alpha.domain.market.values;

import co.com.sofka.domain.generic.ValueObject;
import org.apache.commons.validator.GenericValidator;

public class Country implements ValueObject<String> {

    private final String value;

    public Country(String value) {
        if (GenericValidator.isBlankOrNull(value)){
            throw new IllegalArgumentException("Invalid country name it cannot be empty or null.");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }


}
