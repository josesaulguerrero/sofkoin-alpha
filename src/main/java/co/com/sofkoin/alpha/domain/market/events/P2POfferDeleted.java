package co.com.sofkoin.alpha.domain.market.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P2POfferDeleted extends DomainEvent {
    private String offerId;

    public P2POfferDeleted(String offerId) {
        super(P2POfferDeleted.class.getName());
        this.offerId = offerId;
    }
}