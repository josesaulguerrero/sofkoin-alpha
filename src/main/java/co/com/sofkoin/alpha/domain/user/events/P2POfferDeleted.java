package co.com.sofkoin.alpha.domain.user.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P2POfferDeleted extends DomainEvent {
    private String offerId;
    private String timestamp;

    public P2POfferDeleted(String offerId, String timestamp) {
        super(P2POfferDeleted.class.getName());
        this.offerId = offerId;
        this.timestamp = timestamp;
    }
}
