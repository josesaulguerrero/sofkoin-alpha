package co.com.sofkoin.alpha.domain.market.events;

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

    private String targetAudienceId;

    public P2POfferPublished(String offerId,
                             String publisherId,
                             String cryptoSymbol,
                             Double cryptoAmount,
                             Double cryptoPrice,
                             String targetAudienceId)
    {
        super(P2POfferPublished.class.getName());
        this.offerId = offerId;
        this.publisherId = publisherId;
        this.cryptoSymbol = cryptoSymbol;
        this.cryptoAmount = cryptoAmount;
        this.cryptoPrice = cryptoPrice;
        this.targetAudienceId = targetAudienceId;
    }
}
