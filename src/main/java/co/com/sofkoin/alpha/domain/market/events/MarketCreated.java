package co.com.sofkoin.alpha.domain.market.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarketCreated extends DomainEvent {

    private String marketId;
    private String country;

    public MarketCreated(String marketId, String country) {
        super(MarketCreated.class.getName());
        this.marketId = marketId;
        this.country = country;
    }

}
