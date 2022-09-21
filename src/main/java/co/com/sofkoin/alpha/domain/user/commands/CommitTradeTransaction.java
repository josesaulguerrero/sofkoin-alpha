package co.com.sofkoin.alpha.domain.user.commands;

import co.com.sofka.domain.generic.Command;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommitTradeTransaction extends Command {
    private String buyerId;
    private String transactionType;
    private String cryptoSymbol;
    private Double cryptoAmount;
    private Double cryptoPrice;
    private Double cash;
}
