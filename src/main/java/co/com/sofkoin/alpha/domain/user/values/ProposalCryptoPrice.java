package co.com.sofkoin.alpha.domain.user.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.validator.GenericValidator;

@ToString
@EqualsAndHashCode
public class ProposalCryptoPrice implements ValueObject<Double> {
    private final Double value;

    public ProposalCryptoPrice(Double value) {
        if (!GenericValidator.minValue(value, 0.0000001)) {
            throw new IllegalArgumentException("The minimum amount for an offer cannot be zero or negative.");
        }
        this.value = value;
    }

    @Override
    public Double value() {
        return this.value;
    }
}
