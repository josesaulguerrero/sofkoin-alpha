package co.com.sofkoin.alpha.domain.common.values;

import co.com.sofka.domain.generic.ValueObject;

public class Coin implements ValueObject<Coin.Value> {
    private final String name;
    private final String symbol;

    public Coin(String name, String symbol) {
        if (!name.isBlank() || !name.trim().isEmpty()) {
            throw new IllegalArgumentException("The given name must not be empty or full of just whitespaces.");
        }
        if (!symbol.isBlank() || !symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("The given symbol must not be empty or full of just whitespaces.");
        }
        this.name = name;
        this.symbol = symbol;
    }

    public interface Value {
        String name();
        String symbol();
    }

    @Override
    public Value value() {
        return new Value() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public String symbol() {
                return symbol;
            }
        };
    }
}
