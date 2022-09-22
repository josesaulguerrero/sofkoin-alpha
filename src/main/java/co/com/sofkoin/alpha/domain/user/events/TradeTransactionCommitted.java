package co.com.sofkoin.alpha.domain.user.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TradeTransactionCommitted extends DomainEvent {
    private String transactionId;
    private String buyerId;
    private String transactionType;
    private String cryptoSymbol;
    private Double cryptoAmount;
    private Double cryptoPrice;
    private Double cash;
    private String timestamp;

    public TradeTransactionCommitted() {
        super(TradeTransactionCommitted.class.getName());
    }

    public TradeTransactionCommitted(String transactionId, String buyerId, String transactionType, String cryptoSymbol, Double cryptoAmount, Double cryptoPrice, Double cash, String timestamp) {
        super(TradeTransactionCommitted.class.getName());
        this.transactionId = transactionId;
        this.buyerId = buyerId;
        this.transactionType = transactionType;
        this.cryptoSymbol = cryptoSymbol;
        this.cryptoAmount = cryptoAmount;
        this.cryptoPrice = cryptoPrice;
        this.cash = cash;
        this.timestamp = timestamp;
    }
}
