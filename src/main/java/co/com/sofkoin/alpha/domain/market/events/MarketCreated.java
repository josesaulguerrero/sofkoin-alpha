package co.com.sofkoin.alpha.domain.market.events;

import co.com.sofka.domain.generic.DomainEvent;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarketCreated extends DomainEvent {
    private String country;

    public MarketCreated(String country) {
        super(MarketCreated.class.getName());
        this.country = country;
    }

}
