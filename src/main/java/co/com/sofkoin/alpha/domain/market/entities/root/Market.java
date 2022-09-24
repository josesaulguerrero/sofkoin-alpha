package co.com.sofkoin.alpha.domain.market.entities.root;

import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.market.entities.Offer;
import co.com.sofkoin.alpha.domain.market.events.MarketCreated;
import co.com.sofkoin.alpha.domain.market.events.P2POfferDeleted;
import co.com.sofkoin.alpha.domain.market.events.P2POfferPublished;
import co.com.sofkoin.alpha.domain.market.values.Country;
import co.com.sofkoin.alpha.domain.market.values.OfferCryptoAmount;
import co.com.sofkoin.alpha.domain.market.values.OfferCryptoPrice;
import co.com.sofkoin.alpha.domain.market.values.identities.MarketID;
import co.com.sofkoin.alpha.domain.market.values.identities.OfferId;
import co.com.sofkoin.alpha.domain.user.values.Timestamp;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Market extends AggregateEvent<MarketID> {

    protected Country country;
    protected Set<Offer> offers;
    protected Set<CryptoSymbol> cryptoSymbols;

    public Market(MarketID marketID, Country country){
        super(marketID);
        appendChange(new MarketCreated(country.value(), marketID.value())).apply();
    }

    private Market(MarketID marketID) {
        super(marketID);
        super.subscribe(new MarketEventListener(this));
    }

    public static Market from(MarketID marketID, List<DomainEvent> domainEvents) {
        Market market = new Market(marketID);
        domainEvents.forEach(market::applyEvent);
        return market;
    }

    public void publishP2POffer(OfferId offerId,
                                UserID publisherId,
                                CryptoSymbol cryptoSymbol,
                                OfferCryptoAmount cryptoAmount,
                                OfferCryptoPrice cryptoPrice,
                                UserID targetAudienceId)
    {
        super.appendChange(new P2POfferPublished(offerId.value(),
                publisherId.value(), cryptoSymbol.value(),
                cryptoAmount.value(), cryptoPrice.value(), targetAudienceId.value())).apply();
    }

    public void deleteP2POffer(OfferId offerId){
        super.appendChange(new P2POfferDeleted(offerId.value())).apply();
    }

    public Optional<Offer> getOfferByID(OfferId offerId){
        return offers.stream().filter((offer -> offer.identity().equals(offerId))).findFirst();
    }

}
