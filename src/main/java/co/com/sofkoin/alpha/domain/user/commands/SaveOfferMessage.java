package co.com.sofkoin.alpha.domain.user.commands;

import co.com.sofka.domain.generic.Command;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SaveOfferMessage extends Command {
    private String senderId;
    private String receiverId;
    private String cryptoSymbol;
    private Double cryptoAmount;
    private Double cryptoPrice;
}
