package co.com.sofkoin.alpha.domain.user.commands;

import co.com.sofka.domain.generic.Command;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMessageStatus extends Command {

    private String receiverId;
    private String senderId;
    private String messageId;
    private String newStatus;

}
