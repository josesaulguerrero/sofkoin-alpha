package co.com.sofkoin.alpha.domain.user.entities.root;

import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.domain.common.values.Crypto;
import co.com.sofkoin.alpha.domain.user.entities.Activity;
import co.com.sofkoin.alpha.domain.user.entities.Message;
import co.com.sofkoin.alpha.domain.user.entities.Transaction;
import co.com.sofkoin.alpha.domain.user.events.*;
import co.com.sofkoin.alpha.domain.user.values.*;
import co.com.sofkoin.alpha.domain.user.values.identities.MessageID;
import co.com.sofkoin.alpha.domain.user.values.identities.TransactionID;
import co.com.sofkoin.alpha.domain.user.values.identities.UserID;


import java.util.List;
import java.util.Set;

public class User extends AggregateEvent<UserID> {
    private FullName fullName;
    private Password password;
    private Email email;
    private Phone phone;
    private Cash cash;
    private Avatar avatar;
    private RegisterMethod registerMethod;
    private Set<CryptoBalance> cryptoBalances;
    private Set<Activity> activities;
    private Set<Transaction> transactions;
    private Set<Message> messages;

    public User(UserID entityId, FullName fullName,
                Password password, Email email,
                Phone phone, Avatar avatar,
                RegisterMethod registerMethod) {
        super(entityId);
        super.appendChange(new UserSignedUp(entityId.value(), email.value(), password.value(), fullName.value().name(),
                fullName.value().surname(), phone.value(), avatar.value(), registerMethod.name()));
    }

    private User(UserID userId) {
        super(userId);
        super.subscribe(new UserEventListener(this));
    }

    public static User from(UserID userId, List<DomainEvent> domainEvents) {
        User user = new User(userId);
        domainEvents.forEach(user::applyEvent);
        return user;
    }

    public void changeMessageStatus(MessageID messageId, MessageStatus status) {
        super.appendChange(new MessageStatusChanged(messageId.value(), status.name())).apply();
    }

    public void commitP2PTransaction(TransactionID transactionID,
                                     UserID sellerId, UserID buyerId,
                                     Crypto crypto, TransactionCryptoAmount transactionCryptoAmount,
                                     TransactionCryptoPrice transactionCryptoPrice,
                                     Cash cash, Timestamp timestamp) {
        super.appendChange(new P2PTransactionCommitted(transactionID.value(), sellerId.value(),
                buyerId.value(), crypto.value().symbol(), transactionCryptoAmount.value(),
                transactionCryptoPrice.value(), cash.value(), timestamp.value().toString())).apply();
    }

    public void commitTradeTransaction(TransactionID transactionID,
                                       UserID buyerId,
                                       TransactionTypes transactionTypes,
                                       Crypto crypto, TransactionCryptoAmount transactionCryptoAmount,
                                       TransactionCryptoPrice transactionCryptoPrice,
                                       Cash cash, Timestamp timestamp){



        super.appendChange(new TradeTransactionCommitted(transactionID.value(), buyerId.value(),transactionTypes.name(),
                crypto.value().symbol(), transactionCryptoAmount.value(), transactionCryptoPrice.value(),
                cash.value(), timestamp.value().toString())).apply();
    }

    //TODO: CREATE THE OFFER AGGREGATE
    public void deleteP2POffer(String offerId, Timestamp timestamp){
        super.appendChange(new P2POfferDeleted(offerId, timestamp.value().toString())).apply();
    }

    public void fundWallet(UserID userId, Cash cash, Timestamp timestamp){
        super.appendChange(new WalletFunded(userId.value(),cash.value(),timestamp.value().toString())).apply();
    }

    public void logIn(UserID userId, Email email, Password password, RegisterMethod loginMethod){
        super.appendChange(new UserLoggedIn(userId.value(), email.value(), password.value(), loginMethod.name())).apply();
    }

    public void logOut(UserID userId){
        super.appendChange(new UserLoggedOut(userId.value())).apply();
    }

    public void publishP2POffer(String offerId, UserID publisherId, Crypto cryptoSymbol,
                                TransactionCryptoAmount cryptoAmount, TransactionCryptoPrice cryptoPrice){
        super.appendChange(new P2POfferPublished(offerId,publisherId.value(), cryptoSymbol.value().symbol(),
                cryptoAmount.value(),cryptoPrice.value())).apply();
    }

    public void sendOfferMessage(MessageID messageId, UserID senderId, UserID receiverId,
                                 Crypto cryptoSymbol, TransactionCryptoAmount cryptoAmount, TransactionCryptoPrice cryptoPrice){
        super.appendChange(new OfferMessageSent(messageId.value(), senderId.value(), receiverId.value(),
                cryptoSymbol.value().symbol(), cryptoAmount.value(), cryptoPrice.value())).apply();
    }


}
