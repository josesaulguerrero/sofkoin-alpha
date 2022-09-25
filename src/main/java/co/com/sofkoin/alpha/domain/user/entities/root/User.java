package co.com.sofkoin.alpha.domain.user.entities.root;

import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.market.values.identities.MarketID;
import co.com.sofkoin.alpha.domain.market.values.identities.OfferId;
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
    protected AuthMethod authMethod;
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
                AuthMethod authMethod) {
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
                                authMethod.name()
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

    public Double findCryptoAmountBySymbol(String symbol) {
        CryptoBalance crypto = this.cryptoBalances.stream().filter(cryptoBalance ->
                cryptoBalance.value().coinSymbol().equals(symbol)
        ).findFirst().orElseThrow(() ->
                new IllegalArgumentException("The user doesn't have cryptos with the given symbol.")
        );

        return crypto.value().amount();
    }


    public void validateBuyTransaction(Double cash) {
        if(cash > this.cash.value()) {
            throw new IllegalArgumentException("The user doesn't have enough cash to buy the given crypto.");
        }
    }

    public Message findMessageById(String messageId) {
        return
          this
            .messages().stream()
            .filter(msg -> msg.identity().value().equals(messageId))
            .findFirst().orElseThrow(() ->
                  new IllegalArgumentException("The message with the given ID doesn't exist in this user.")
            );
    }
    public void validateSellTransaction(Double transactionCryptoAmount, String cryptoSymbol) {
        Double userCryptoAmount = this.findCryptoAmountBySymbol(cryptoSymbol);

        if(transactionCryptoAmount > userCryptoAmount) {
            throw new IllegalArgumentException("The user doesn't have enough crypto to sell to the exchange.");
        }
    }

    public void changeMessageStatus(UserID receiverId, UserID senderId, MessageID messageId, MessageStatus newStatus) {
        super
                .appendChange(
                        new MessageStatusChanged(
                                receiverId.value(),
                                senderId.value(),
                                messageId.value(),
                                newStatus.name()
                        )
                )
                .apply();
    }

    public void commitP2PTransaction(TransactionID transactionID,
                                     UserID sellerId,
                                     UserID buyerId,
                                     OfferId offerId,
                                     MarketID marketId,
                                     CryptoSymbol cryptoSymbol,
                                     TransactionCryptoAmount transactionCryptoAmount,
                                     TransactionCryptoPrice transactionCryptoPrice,
                                     String transactionType,
                                     Cash cash,
                                     Timestamp timestamp) {
        super
                .appendChange(
                        new P2PTransactionCommitted(
                                transactionID.value(),
                                sellerId.value(),
                                buyerId.value(),
                                offerId.value(),
                                marketId.value(),
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

    public void logIn(UserID userId, Email email, AuthMethod loginMethod, String jwt) {
        super
                .appendChange(
                        new UserLoggedIn(
                                userId.value(),
                                email.value(),
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

    public void saveOfferMessage(MessageID messageId,
                                 MarketID marketID,
                                 UserID senderId,
                                 UserID receiverId,
                                 CryptoSymbol cryptoSymbol,
                                 TransactionCryptoAmount cryptoAmount,
                                 TransactionCryptoPrice cryptoPrice
    ) {
        super
                .appendChange(
                        new OfferMessageSaved(
                                messageId.value(),
                                marketID.value(),
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

    public AuthMethod authMethod() {
        return authMethod;
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
