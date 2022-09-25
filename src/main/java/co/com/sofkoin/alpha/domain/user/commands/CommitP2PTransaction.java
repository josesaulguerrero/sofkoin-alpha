package co.com.sofkoin.alpha.domain.user.commands;

import co.com.sofka.domain.generic.Command;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommitP2PTransaction extends Command {

    private String sellerId;
    private String buyerId;
    private String marketId;
    private String offerId;
    private String cryptoSymbol;
    private Double cryptoAmount;
    private Double cryptoPrice;
    private String transactionType;

}
