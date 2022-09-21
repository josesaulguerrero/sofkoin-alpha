package co.com.sofkoin.alpha.domain.user.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.validator.GenericValidator;

@ToString
@EqualsAndHashCode
public class TransactionCryptoAmount implements ValueObject<Double> {
    private final Double value;

    public TransactionCryptoAmount(Double value) {
        if (!GenericValidator.minValue(value, 0.0)) {
            throw new IllegalArgumentException("The Transaction amount must be over 0.");
        }
        this.value = value;
    }

    @Override
    public Double value() {
        return this.value;
    }
}
