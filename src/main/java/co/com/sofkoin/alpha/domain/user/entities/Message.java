package co.com.sofkoin.alpha.domain.user.entities;

import co.com.sofka.domain.generic.Entity;
import co.com.sofkoin.alpha.domain.user.values.MessageStatus;
import co.com.sofkoin.alpha.domain.user.values.ProposalCryptoAmount;
import co.com.sofkoin.alpha.domain.user.values.ProposalCryptoPrice;
import co.com.sofkoin.alpha.domain.user.values.identities.MessageID;
import co.com.sofkoin.alpha.domain.user.values.identities.UserID;
import co.com.sofkoin.alpha.domain.user.values.values.Crypto;

public class Message extends Entity<MessageID> {
    private final ProposalCryptoAmount proposalCryptoAmount;

    private final ProposalCryptoPrice proposalCryptoPrice;

    private MessageStatus messageStatus;
    
    private final UserID senderId;
    
    private final UserID receiverId;
    
    private final Crypto crypto;

    public Message(MessageID entityId, ProposalCryptoAmount proposalCryptoAmount,
                   ProposalCryptoPrice proposalCryptoPrice, MessageStatus messageStatus,
                   UserID senderId, UserID receiverId, Crypto crypto) {
        super(entityId);
        this.proposalCryptoAmount = proposalCryptoAmount;
        this.proposalCryptoPrice = proposalCryptoPrice;
        this.messageStatus = messageStatus;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.crypto = crypto;
    }

    public void changeStatus(MessageStatus messageStatus){
        this.messageStatus = messageStatus;
    }

    public ProposalCryptoAmount proposalCryptoAmount() {
        return proposalCryptoAmount;
    }

    public ProposalCryptoPrice proposalCryptoPrice() {
        return proposalCryptoPrice;
    }

    public MessageStatus messageStatus() {
        return messageStatus;
    }

    public UserID senderId() {
        return senderId;
    }

    public UserID receiverId() {
        return receiverId;
    }

    public Crypto crypto() {
        return crypto;
    }
}
