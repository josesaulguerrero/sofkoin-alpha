package co.com.sofkoin.alpha.domain.user.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.*;

@Getter
@Setter
@ToString
public class UserSignedUp extends DomainEvent {
    private String userId;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private String avatarUrl;
    private String registerMethod;

    public UserSignedUp() {
        super(UserSignedUp.class.getName());
    }

    public UserSignedUp(String userId,
                        String email,
                        String password,
                        String name,
                        String surname,
                        String phoneNumber,
                        String avatarUrl,
                        String registerMethod) {
        super(UserSignedUp.class.getName());
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.avatarUrl = avatarUrl;
        this.registerMethod = registerMethod;
    }
}
