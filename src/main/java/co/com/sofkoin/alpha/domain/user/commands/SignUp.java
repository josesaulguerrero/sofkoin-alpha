package co.com.sofkoin.alpha.domain.user.commands;

import co.com.sofka.domain.generic.Command;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SignUp extends Command {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private String avatarUrl;
    private String registerMethod;
}
