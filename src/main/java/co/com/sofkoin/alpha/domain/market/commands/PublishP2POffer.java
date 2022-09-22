package co.com.sofkoin.alpha.domain.market.commands;

import co.com.sofka.domain.generic.Command;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PublishP2POffer extends Command {
    private String marketId;
    private String offerId;
    private String publisherId;
    private String targetAudienceId;
    private String cryptoSymbol;
    private Double offerCryptoAmount;
    private Double offerCryptoPrice;
}
