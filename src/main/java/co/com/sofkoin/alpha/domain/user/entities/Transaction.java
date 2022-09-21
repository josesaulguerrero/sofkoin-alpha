package co.com.sofkoin.alpha.domain.user.entities;

import co.com.sofka.domain.generic.Entity;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.user.values.Timestamp;
import co.com.sofkoin.alpha.domain.user.values.TransactionCryptoAmount;
import co.com.sofkoin.alpha.domain.user.values.TransactionCryptoPrice;
import co.com.sofkoin.alpha.domain.user.values.TransactionTypes;
import co.com.sofkoin.alpha.domain.user.values.identities.TransactionID;


public class Transaction extends Entity<TransactionID> {
    private final Timestamp timestamp;
    
    private final TransactionTypes type;
    
    private final TransactionCryptoAmount transactionCryptoAmount;
    
    private final TransactionCryptoPrice transactionCryptoPrice;
    
    private final CryptoSymbol cryptoSymbol;

    public Transaction(TransactionID entityId, Timestamp timestamp,
                       TransactionTypes type, TransactionCryptoAmount transactionCryptoAmount,
                       TransactionCryptoPrice transactionCryptoPrice, CryptoSymbol cryptoSymbol) {
        super(entityId);
        this.timestamp = timestamp;
        this.type = type;
        this.transactionCryptoAmount = transactionCryptoAmount;
        this.transactionCryptoPrice = transactionCryptoPrice;
        this.cryptoSymbol = cryptoSymbol;
    }

    public Timestamp timestamp() {
        return timestamp;
    }

    public TransactionTypes type() {
        return type;
    }

    public TransactionCryptoAmount transactionAmount() {
        return transactionCryptoAmount;
    }

    public TransactionCryptoPrice transactionCryptoPrice() {
        return transactionCryptoPrice;
    }

    public CryptoSymbol crypto() {
        return cryptoSymbol;
    }
}
