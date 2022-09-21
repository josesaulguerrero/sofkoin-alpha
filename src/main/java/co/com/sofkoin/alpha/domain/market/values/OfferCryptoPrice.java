package co.com.sofkoin.alpha.domain.market.values;

import co.com.sofka.domain.generic.ValueObject;
import org.apache.commons.validator.GenericValidator;

public class OfferCryptoPrice implements ValueObject<Double> {

    private final Double value;

    public OfferCryptoPrice(Double value) {
        if (!GenericValidator.minValue(value, 0.0)) {
            throw new IllegalArgumentException("Offer crypto price cannot be negative.");
        }
        this.value = value;
    }

    @Override
    public Double value() {
        return this.value;
    }

}
