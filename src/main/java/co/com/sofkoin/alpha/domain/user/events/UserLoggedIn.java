package co.com.sofkoin.alpha.domain.user.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserLoggedIn extends DomainEvent {
    private String userId;
    private String email;
    private String password;
    private String authMethod;
    private String jwt;

    public UserLoggedIn() {
        super(UserLoggedIn.class.getName());
    }

    public UserLoggedIn(String userId, String email, String password, String authMethod, String jwt) {
        super(UserLoggedIn.class.getName());
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authMethod = authMethod;
        this.jwt = jwt;
    }
}
