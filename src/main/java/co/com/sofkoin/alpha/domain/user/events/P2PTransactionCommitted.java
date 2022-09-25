package co.com.sofkoin.alpha.domain.user.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P2PTransactionCommitted extends DomainEvent {
    private String transactionId;
    private String transactionType;
    private String sellerId;
    private String buyerId;
    private String offerId;
    private String marketId;
    private String cryptoSymbol;
    private Double cryptoAmount;
    private Double cryptoPrice;
    private Double cash;
    private String timestamp;

    public P2PTransactionCommitted() {
        super(P2PTransactionCommitted.class.getName());
    }

    public P2PTransactionCommitted(String transactionId,
                                   String sellerId,
                                   String buyerId,
                                   String offerId,
                                   String marketId,
                                   String cryptoSymbol,
                                   Double cryptoAmount,
                                   Double cryptoPrice,
                                   String transactionType,
                                   Double cash,
                                   String timestamp) {
        super(P2PTransactionCommitted.class.getName());
        this.transactionId = transactionId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.offerId = offerId;
        this.marketId = marketId;
        this.cryptoSymbol = cryptoSymbol;
        this.cryptoAmount = cryptoAmount;
        this.cryptoPrice = cryptoPrice;
        this.transactionType = transactionType;
        this.cash = cash;
        this.timestamp = timestamp;
    }
}
