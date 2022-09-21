package co.com.sofkoin.alpha.domain.market.entities.root;

import co.com.sofka.domain.generic.EventChange;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.market.entities.Offer;
import co.com.sofkoin.alpha.domain.market.events.P2POfferDeleted;
import co.com.sofkoin.alpha.domain.market.events.P2POfferPublished;
import co.com.sofkoin.alpha.domain.market.values.OfferCryptoAmount;
import co.com.sofkoin.alpha.domain.market.values.OfferCryptoPrice;
import co.com.sofkoin.alpha.domain.market.values.identities.OfferId;
import java.util.Objects;

public class MarketEventListener extends EventChange {

    public MarketEventListener(Market market){

        super.apply((P2POfferPublished event) -> {
            Offer offer = new Offer(new OfferId(event.getOfferId()),
                    new UserID(event.getPublisherId()),
                    new CryptoSymbol(event.getCryptoSymbol()),
                    new OfferCryptoAmount(event.getCryptoAmount()),
                    new OfferCryptoPrice(event.getCryptoPrice()),
                    new UserID(event.getTargetAudienceId()));
            market.offers.add(offer);
        });

        super.apply((P2POfferDeleted event) -> {
            Offer offerToDelete  = market.offers
                    .stream()
                    .filter(offer -> Objects.equals(offer.identity().value(), event.getOfferId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("The given id does not belong to any active offer."));
            market.offers.remove(offerToDelete);
        });

    }

}
