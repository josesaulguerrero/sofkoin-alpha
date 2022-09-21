package co.com.sofkoin.alpha.domain.user.entities;

import co.com.sofka.domain.generic.Entity;
import co.com.sofkoin.alpha.domain.common.values.Crypto;
import co.com.sofkoin.alpha.domain.user.values.Timestamp;
import co.com.sofkoin.alpha.domain.user.values.TransactionCryptoAmount;
import co.com.sofkoin.alpha.domain.user.values.TransactionCryptoPrice;
import co.com.sofkoin.alpha.domain.user.values.TransactionTypes;
import co.com.sofkoin.alpha.domain.user.values.identities.TransactionID;


public class Transaction extends Entity<TransactionID> {
    private final Timestamp timestamp;
    
    private final TransactionTypes transactionTypes;
    
    private final TransactionCryptoAmount transactionCryptoAmount;
    
    private final TransactionCryptoPrice transactionCryptoPrice;
    
    private final Crypto crypto;

    public Transaction(TransactionID entityId, Timestamp timestamp,
                       TransactionTypes transactionTypes, TransactionCryptoAmount transactionCryptoAmount,
                       TransactionCryptoPrice transactionCryptoPrice, Crypto crypto) {
        super(entityId);
        this.timestamp = timestamp;
        this.transactionTypes = transactionTypes;
        this.transactionCryptoAmount = transactionCryptoAmount;
        this.transactionCryptoPrice = transactionCryptoPrice;
        this.crypto = crypto;
    }

    public Timestamp timestamp() {
        return timestamp;
    }

    public TransactionTypes transactionTypes() {
        return transactionTypes;
    }

    public TransactionCryptoAmount transactionAmount() {
        return transactionCryptoAmount;
    }

    public TransactionCryptoPrice transactionCryptoPrice() {
        return transactionCryptoPrice;
    }

    public Crypto crypto() {
        return crypto;
    }
}
