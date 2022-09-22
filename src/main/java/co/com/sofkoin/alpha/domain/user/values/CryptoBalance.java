package co.com.sofkoin.alpha.domain.user.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.validator.GenericValidator;

@ToString
@EqualsAndHashCode
public class
CryptoBalance implements ValueObject<CryptoBalance.Value> {
    private final Double amount;
    private final String coinSymbol;

    public CryptoBalance(Double amount, String coinSymbol) {
        if (amount < 0.0) {
            throw new IllegalArgumentException("Crypto amount cannot be negative.");
        }
        this.amount = amount;
        this.coinSymbol = coinSymbol;
    }

    public interface Value {
        Double amount();

        String coinSymbol();
    }

    @Override
    public Value value() {
        return new Value() {
            @Override
            public Double amount() {
                return amount;
            }

            @Override
            public String coinSymbol() {
                return coinSymbol;
            }
        };
    }
}
