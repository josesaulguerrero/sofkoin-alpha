package co.com.sofkoin.alpha.domain.user.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OfferMessageSaved extends DomainEvent {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String cryptoSymbol;
    private Double cryptoAmount;
    private Double cryptoPrice;

    public OfferMessageSaved() {
        super(OfferMessageSaved.class.getName());
    }

    public OfferMessageSaved(String messageId, String senderId, String receiverId, String cryptoSymbol, Double cryptoAmount, Double cryptoPrice) {
        super(OfferMessageSaved.class.getName());
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.cryptoSymbol = cryptoSymbol;
        this.cryptoAmount = cryptoAmount;
        this.cryptoPrice = cryptoPrice;
    }
}
