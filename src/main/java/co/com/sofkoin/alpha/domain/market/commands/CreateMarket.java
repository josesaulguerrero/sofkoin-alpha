package co.com.sofkoin.alpha.domain.market.commands;

import co.com.sofka.domain.generic.Command;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateMarket extends Command {

    private String country;

}
