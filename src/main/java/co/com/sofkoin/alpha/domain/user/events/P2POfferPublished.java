package co.com.sofkoin.alpha.domain.user.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P2POfferPublished extends DomainEvent {
    private String offerId;
    private String publisherId;
    private String cryptoSymbol;
    private Double cryptoAmount;
    private Double cryptoPrice;

    public P2POfferPublished(String offerId, String publisherId, String cryptoSymbol, Double cryptoAmount, Double cryptoPrice) {
        super(P2POfferPublished.class.getName());
        this.offerId = offerId;
        this.publisherId = publisherId;
        this.cryptoSymbol = cryptoSymbol;
        this.cryptoAmount = cryptoAmount;
        this.cryptoPrice = cryptoPrice;
    }
}
