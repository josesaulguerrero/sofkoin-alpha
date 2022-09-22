package co.com.sofkoin.alpha.domain.user.entities.root;

import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.user.entities.Activity;
import co.com.sofkoin.alpha.domain.user.entities.Message;
import co.com.sofkoin.alpha.domain.user.entities.Transaction;
import co.com.sofkoin.alpha.domain.user.events.*;
import co.com.sofkoin.alpha.domain.user.values.*;
import co.com.sofkoin.alpha.domain.user.values.identities.MessageID;
import co.com.sofkoin.alpha.domain.user.values.identities.TransactionID;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import java.util.List;
import java.util.Set;

@ToString
@EqualsAndHashCode(callSuper = true)
public class User extends AggregateEvent<UserID> {
    protected FullName fullName;
    protected Password password;
    protected Email email;
    protected Phone phone;
    protected Cash cash;
    protected Avatar avatar;
    protected RegisterMethod registerMethod;
    protected Set<CryptoBalance> cryptoBalances;
    protected Set<Activity> activities;
    protected Set<Transaction> transactions;
    protected Set<Message> messages;

    public User(UserID entityId,
                FullName fullName,
                Password password,
                Email email,
                Phone phone,
                Avatar avatar,
                RegisterMethod registerMethod)
    {
        super(entityId);
        super
                .appendChange(
                        new UserSignedUp(
                                entityId.value(),
                                email.value(),
                                password.value(),
                                fullName.value().name(),
                                fullName.value().surname(),
                                phone.value(),
                                avatar.value(),
                                registerMethod.name()
                        )
                )
                .apply();
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
        super
                .appendChange(
                        new MessageStatusChanged(
                                messageId.value(),
                                status.name()
                        )
                )
                .apply();
    }

    public void commitP2PTransaction(TransactionID transactionID, UserID sellerId, UserID buyerId, CryptoSymbol cryptoSymbol, TransactionCryptoAmount transactionCryptoAmount, TransactionCryptoPrice transactionCryptoPrice, String transactionType, Cash cash, Timestamp timestamp) {
        super
                .appendChange(
                        new P2PTransactionCommitted(
                                transactionID.value(),
                                sellerId.value(),
                                buyerId.value(),
                                cryptoSymbol.value(),
                                transactionCryptoAmount.value(),
                                transactionCryptoPrice.value(),
                                transactionType,
                                cash.value(),
                                timestamp.value().toString()
                        )
                )
                .apply();
    }

    public void commitTradeTransaction(TransactionID transactionID, UserID buyerId, TransactionTypes transactionTypes, CryptoSymbol cryptoSymbol, TransactionCryptoAmount transactionCryptoAmount, TransactionCryptoPrice transactionCryptoPrice, Cash cash, Timestamp timestamp) {
        super
                .appendChange(
                        new TradeTransactionCommitted(
                                transactionID.value(),
                                buyerId.value(),
                                transactionTypes.name(),
                                cryptoSymbol.value(),
                                transactionCryptoAmount.value(),
                                transactionCryptoPrice.value(),
                                cash.value(),
                                timestamp.value().toString()
                        )
                )
                .apply();
    }

    public void fundWallet(UserID userId, Cash cash, Timestamp timestamp) {
        super
                .appendChange(
                        new WalletFunded(
                                userId.value(),
                                cash.value(),
                                timestamp.value().toString()
                        )
                )
                .apply();
    }

    public void logIn(UserID userId, Email email, Password password, RegisterMethod loginMethod, String jwt) {
        super
                .appendChange(
                        new UserLoggedIn(
                                userId.value(),
                                email.value(),
                                password.value(),
                                loginMethod.name(),
                                jwt
                        )
                )
                .apply();
    }

    public void logOut(UserID userId) {
        super
                .appendChange(
                        new UserLoggedOut(userId.value())
                )
                .apply();
    }

    public void saveOfferMessage(MessageID messageId, UserID senderId, UserID receiverId, CryptoSymbol cryptoSymbol, TransactionCryptoAmount cryptoAmount, TransactionCryptoPrice cryptoPrice) {
        super
                .appendChange(
                        new OfferMessageSaved(
                                messageId.value(),
                                senderId.value(),
                                receiverId.value(),
                                cryptoSymbol.value(),
                                cryptoAmount.value(),
                                cryptoPrice.value()
                        )
                )
                .apply();
    }

  public FullName fullName() {
    return fullName;
  }

  public Password password() {
    return password;
  }

  public Email email() {
    return email;
  }

  public Phone phone() {
    return phone;
  }

  public Cash cash() {
    return cash;
  }

  public Avatar avatar() {
    return avatar;
  }

  public RegisterMethod registerMethod() {
    return registerMethod;
  }

  public Set<CryptoBalance> cryptoBalances() {
    return cryptoBalances;
  }

  public Set<Activity> activities() {
    return activities;
  }

  public Set<Transaction> transactions() {
    return transactions;
  }

  public Set<Message> messages() {
    return messages;
  }
}
