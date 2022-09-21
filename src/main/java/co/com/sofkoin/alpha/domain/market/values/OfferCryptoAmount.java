package co.com.sofkoin.alpha.domain.market.values;

import co.com.sofka.domain.generic.ValueObject;
import org.apache.commons.validator.GenericValidator;

public class OfferCryptoAmount implements ValueObject<Double> {

    private final Double value;

    public OfferCryptoAmount(Double value) {
        if (!GenericValidator.minValue(value, 0.0)) {
            throw new IllegalArgumentException("The offer amount must be over 0.");
        }
        this.value = value;
    }

    @Override
    public Double value() {
        return this.value;
    }


}
