package co.com.sofkoin.alpha.domain.user.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WalletFunded extends DomainEvent {
    private String userId;
    private Double cashAmount;
    private String timestamp;

    public WalletFunded() {
        super(WalletFunded.class.getName());
    }

    public WalletFunded(String userId, Double cashAmount, String timestamp) {
        super(WalletFunded.class.getName());
        this.userId = userId;
        this.cashAmount = cashAmount;
        this.timestamp = timestamp;
    }
}
