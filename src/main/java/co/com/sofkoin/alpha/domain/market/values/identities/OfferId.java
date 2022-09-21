package co.com.sofkoin.alpha.domain.market.values.identities;

import co.com.sofka.domain.generic.Identity;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class OfferId extends Identity {

    public OfferId() {
        super();
    }

    public OfferId(String value) {
        super(value);
    }

}
