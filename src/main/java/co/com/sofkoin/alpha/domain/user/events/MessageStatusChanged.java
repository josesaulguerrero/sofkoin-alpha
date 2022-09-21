package co.com.sofkoin.alpha.domain.user.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageStatusChanged extends DomainEvent {
    private String senderId;
    private String receiverId;
    private String messageId;
    private String newStatus;

    public MessageStatusChanged(String senderId, String receiverId, String messageId, String newStatus) {
        super(MessageStatusChanged.class.getName());
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageId = messageId;
        this.newStatus = newStatus;
    }
}
