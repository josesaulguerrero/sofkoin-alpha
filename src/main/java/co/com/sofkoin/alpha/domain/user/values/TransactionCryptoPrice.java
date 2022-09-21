package co.com.sofkoin.alpha.domain.user.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.validator.GenericValidator;

@ToString
@EqualsAndHashCode
public class TransactionCryptoPrice implements ValueObject<Double> {
    private final Double value;

    public TransactionCryptoPrice(Double value) {
        if (!GenericValidator.minValue(value, 0.0)) {
            throw new IllegalArgumentException("Crypto price cannot be negative.");
        }
        this.value = value;
    }

    @Override
    public Double value() {
        return this.value;
    }
}
