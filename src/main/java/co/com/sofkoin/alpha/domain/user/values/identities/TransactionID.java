package co.com.sofkoin.alpha.domain.user.values.identities;

import co.com.sofka.domain.generic.Identity;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class TransactionID extends Identity {
    public TransactionID() {
        super();
    }

    public TransactionID(String uuid) {
        super(uuid);
    }
}
