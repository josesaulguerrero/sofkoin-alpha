package co.com.sofkoin.alpha.domain.user.values.identities;

import co.com.sofka.domain.generic.Identity;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ActivityID extends Identity {
    public ActivityID() {
        super();
    }

    public ActivityID(String uuid) {
        super(uuid);
    }
}
