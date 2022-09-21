package co.com.sofkoin.alpha.domain.user.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.validator.GenericValidator;

@ToString
@EqualsAndHashCode
public class ProposalCryptoAmount implements ValueObject<Double> {
    private final Double value;

    public ProposalCryptoAmount(Double value) {
        if (!GenericValidator.minValue(value, 0.0000001)) {
            throw new IllegalArgumentException("You cannot buy zero or negative values in cryptos.");
        }
        this.value = value;
    }

    @Override
    public Double value() {
        return this.value;
    }
}
