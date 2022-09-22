package co.com.sofkoin.alpha.domain.user.entities.root;

import co.com.sofka.domain.generic.EventChange;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.user.entities.Activity;
import co.com.sofkoin.alpha.domain.user.entities.Message;
import co.com.sofkoin.alpha.domain.user.entities.Transaction;
import co.com.sofkoin.alpha.domain.user.events.*;
import co.com.sofkoin.alpha.domain.user.values.*;
import co.com.sofkoin.alpha.domain.user.values.identities.ActivityID;
import co.com.sofkoin.alpha.domain.user.values.identities.MessageID;
import co.com.sofkoin.alpha.domain.user.values.identities.TransactionID;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

public class UserEventListener extends EventChange {
    public UserEventListener(User user) {

        super.apply((UserSignedUp event) -> {
            user.fullName = new FullName(event.getName(), event.getSurname());
            user.password = new Password(event.getPassword());
            user.email = new Email(event.getEmail());
            user.phone = new Phone(event.getPhoneNumber());
            user.cash = new Cash(0.0);
            user.avatar = new Avatar(event.getAvatarUrl());
            user.registerMethod = RegisterMethod.valueOf(
                    event.getRegisterMethod().toUpperCase(Locale.ROOT).trim()
            );
            user.cryptoBalances = new HashSet<>();
            user.activities = new HashSet<>();
            user.transactions = new HashSet<>();
            user.messages = new HashSet<>();
        });

        super.apply((MessageStatusChanged event) -> {
            Message message = user.messages
                    .stream()
                    .filter(m -> Objects.equals(m.identity().value(), event.getMessageId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("The given id does not belong to any of the user messages."));
            MessageStatus newStatus = MessageStatus.valueOf(
                    event.getNewStatus().toUpperCase(Locale.ROOT).trim()
            );
            message.changeStatus(newStatus);
        });

        super.apply((P2PTransactionCommitted event) -> {
            Transaction transaction = new Transaction(
                    new TransactionID(),
                    new Timestamp(),
                    TransactionTypes.valueOf(event.getTransactionType().toUpperCase(Locale.ROOT).trim()),
                    new TransactionCryptoAmount(event.getCryptoAmount()),
                    new TransactionCryptoPrice(event.getCryptoPrice()),
                    new CryptoSymbol(event.getCryptoSymbol())
            );
            user.transactions.add(transaction);
            if (transaction.type().equals(TransactionTypes.BUY)) {
                CryptoBalance currentCryptoBalance = user.cryptoBalances
                        .stream()
                        .filter(c -> Objects.equals(c.value().coinSymbol(), event.getCryptoSymbol()))
                        .findFirst()
                        .orElseGet(() -> new CryptoBalance(0.0, event.getCryptoSymbol()));
                CryptoBalance newCryptoBalance = new CryptoBalance(
                        currentCryptoBalance.value().amount() + event.getCryptoAmount(),
                        currentCryptoBalance.value().coinSymbol()
                );
                user.cash = new Cash(user.cash.value() - event.getCash());
                user.cryptoBalances.remove(currentCryptoBalance);
                user.cryptoBalances.add(newCryptoBalance);
            } else if (transaction.type().equals(TransactionTypes.SELL)) {
                CryptoBalance currentCryptoBalance = user.cryptoBalances
                        .stream()
                        .filter(c -> Objects.equals(c.value().coinSymbol(), event.getCryptoSymbol()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("The user does not have any coins with the specified name; You cannot sell coins you do not possess."));
                CryptoBalance newCryptoBalance = new CryptoBalance(
                        currentCryptoBalance.value().amount() - event.getCryptoAmount(),
                        currentCryptoBalance.value().coinSymbol()
                );
                user.cash = new Cash(user.cash.value() + event.getCash());
                user.cryptoBalances.remove(currentCryptoBalance);
                user.cryptoBalances.add(newCryptoBalance);
            }
        });

        super.apply((TradeTransactionCommitted event) -> {
            Transaction transaction = new Transaction(
                    new TransactionID(),
                    new Timestamp(),
                    TransactionTypes.valueOf(event.getTransactionType().toUpperCase(Locale.ROOT).trim()),
                    new TransactionCryptoAmount(event.getCryptoAmount()),
                    new TransactionCryptoPrice(event.getCryptoPrice()),
                    new CryptoSymbol(event.getCryptoSymbol())
            );
            user.transactions.add(transaction);
            if (transaction.type().equals(TransactionTypes.BUY)) {
                CryptoBalance currentCryptoBalance = user.cryptoBalances
                        .stream()
                        .filter(c -> Objects.equals(c.value().coinSymbol(), event.getCryptoSymbol()))
                        .findFirst()
                        .orElseGet(() -> new CryptoBalance(0.0, event.getCryptoSymbol()));
                CryptoBalance newCryptoBalance = new CryptoBalance(
                        currentCryptoBalance.value().amount() + event.getCryptoAmount(),
                        currentCryptoBalance.value().coinSymbol()
                );
                user.cash = new Cash(user.cash.value() - event.getCash());
                user.cryptoBalances.remove(currentCryptoBalance);
                user.cryptoBalances.add(newCryptoBalance);
            } else if (transaction.type().equals(TransactionTypes.SELL)) {
                CryptoBalance currentCryptoBalance = user.cryptoBalances
                        .stream()
                        .filter(c -> Objects.equals(c.value().coinSymbol(), event.getCryptoSymbol()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("The user does not have any coins with the " +
                                "specified name; You cannot sell coins you do not possess."));
                CryptoBalance newCryptoBalance = new CryptoBalance(
                        currentCryptoBalance.value().amount() - event.getCryptoAmount(),
                        currentCryptoBalance.value().coinSymbol()
                );
                user.cash = new Cash(user.cash.value() + event.getCash());
                user.cryptoBalances.remove(currentCryptoBalance);
                user.cryptoBalances.add(newCryptoBalance);
            }
        });

        super.apply((WalletFunded event) -> {
            user.cash = new Cash(user.cash.value() + event.getCashAmount());
        });

        super.apply((UserLoggedIn event) -> {
            Activity login = new Activity(new ActivityID(), new Timestamp(), ActivityTypes.LOGIN);
            user.activities.add(login);
        });

        super.apply((UserLoggedOut event) -> {
            Activity logout = new Activity(new ActivityID(), new Timestamp(), ActivityTypes.LOGOUT);
            user.activities.add(logout);
        });

        super.apply((OfferMessageSaved event) -> {
            Message message = new Message(
                    new MessageID(),
                    new ProposalCryptoAmount(event.getCryptoAmount()),
                    new ProposalCryptoPrice(event.getCryptoPrice()),
                    MessageStatus.PENDING,
                    new UserID(event.getSenderId()),
                    new UserID(event.getReceiverId()),
                    new CryptoSymbol(event.getCryptoSymbol())
            );
            user.messages.add(message);
            //Falta añadir el mensaje al segundo usuario
        });
    }
}
