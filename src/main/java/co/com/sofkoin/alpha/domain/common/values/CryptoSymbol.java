package co.com.sofkoin.alpha.domain.common.values;

import co.com.sofka.domain.generic.ValueObject;

public class CryptoSymbol implements ValueObject<String> {
    private final String value;

    public CryptoSymbol(String value) {
        if (value.isBlank() || value.trim().isEmpty()) {
            throw new IllegalArgumentException("The given symbol must not be empty or full of just whitespaces.");
        }
        this.value = value;
    }

    @Override
    public String value() {
        return this.value;
    }
}
