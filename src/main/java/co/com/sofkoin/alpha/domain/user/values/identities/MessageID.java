package co.com.sofkoin.alpha.domain.user.values.identities;

import co.com.sofka.domain.generic.Identity;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class MessageID extends Identity {
    public MessageID() {
        super();
    }

    public MessageID(String uuid) {
        super(uuid);
    }
}
