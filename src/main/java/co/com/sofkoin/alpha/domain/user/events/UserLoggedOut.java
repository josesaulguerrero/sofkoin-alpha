package co.com.sofkoin.alpha.domain.user.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserLoggedOut extends DomainEvent {
    private String userId;

    public UserLoggedOut(String userId) {
        super(UserLoggedOut.class.getName());
        this.userId = userId;
    }
}
