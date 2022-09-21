package co.com.sofkoin.alpha.domain.user.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageStatusChanged extends DomainEvent {
    private String messageId;
    private String newStatus;

    public MessageStatusChanged(String messageId, String newStatus) {
        super(MessageStatusChanged.class.getName());
        this.messageId = messageId;
        this.newStatus = newStatus;
    }
}
