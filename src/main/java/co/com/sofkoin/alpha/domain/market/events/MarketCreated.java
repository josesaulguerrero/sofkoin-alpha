package co.com.sofkoin.alpha.domain.market.events;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarketCreated extends DomainEvent {
    private String marketId;
    private String country;

    public MarketCreated() {
        super(MarketCreated.class.getName());
    }

    public MarketCreated(String country, String marketId) {
        super(MarketCreated.class.getName());
        this.country = country;
        this.marketId = marketId;
    }
}
