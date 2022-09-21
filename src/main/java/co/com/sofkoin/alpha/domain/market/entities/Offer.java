package co.com.sofkoin.alpha.domain.market.entities;

import co.com.sofka.domain.generic.Entity;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.market.values.OfferCryptoAmount;
import co.com.sofkoin.alpha.domain.market.values.OfferCryptoPrice;
import co.com.sofkoin.alpha.domain.market.values.identities.OfferId;

public class Offer extends Entity<OfferId> {

    private UserID publisherId;
    private CryptoSymbol cryptoSymbol;
    private OfferCryptoAmount cryptoAmount;
    private OfferCryptoPrice cryptoPrice;
    private UserID targetAudienceId;

    public Offer(OfferId entityId,
                 UserID publisherId,
                 CryptoSymbol cryptoSymbol,
                 OfferCryptoAmount cryptoAmount,
                 OfferCryptoPrice cryptoPrice,
                 UserID targetAudienceId) {
        super(entityId);
        this.publisherId = publisherId;
        this.cryptoSymbol = cryptoSymbol;
        this.cryptoAmount = cryptoAmount;
        this.cryptoPrice = cryptoPrice;
        this.targetAudienceId = targetAudienceId;
    }

    public UserID publisherId() {
        return publisherId;
    }

    public CryptoSymbol cryptoSymbol() {
        return cryptoSymbol;
    }

    public OfferCryptoAmount cryptoAmount() {
        return cryptoAmount;
    }

    public OfferCryptoPrice cryptoPrice() {
        return cryptoPrice;
    }

    public UserID targetAudienceId() {
        return targetAudienceId;
    }
}
