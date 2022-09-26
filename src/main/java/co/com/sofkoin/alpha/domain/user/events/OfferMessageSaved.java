package co.com.sofkoin.alpha.domain.user.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OfferMessageSaved extends DomainEvent {
    private String marketId;
    private String messageId;
    private String senderId;
    private String receiverId;
    private String cryptoSymbol;

    private String messageRelationType;
    private Double cryptoAmount;
    private Double cryptoPrice;

    public OfferMessageSaved() {
        super(OfferMessageSaved.class.getName());
    }

    public OfferMessageSaved(String messageId, String marketId, String senderId, String receiverId, String cryptoSymbol, String messageRelationType, Double cryptoAmount, Double cryptoPrice) {
        super(OfferMessageSaved.class.getName());
        this.marketId = marketId;
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageRelationType = messageRelationType;
        this.cryptoSymbol = cryptoSymbol;
        this.cryptoAmount = cryptoAmount;
        this.cryptoPrice = cryptoPrice;
    }
}
