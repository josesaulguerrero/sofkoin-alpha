package co.com.sofkoin.alpha.domain.market.values.identities;

import co.com.sofka.domain.generic.Identity;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class MarketID extends Identity {

    public MarketID() {
        super();
    }

    public MarketID(String value) {
        super(value);
    }

}
