package co.com.sofkoin.alpha.domain.user.commands;

import co.com.sofka.domain.generic.Command;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PublishP2POffer extends Command {
    private String publisherId;
    private String targetAudienceId;
    private Double cryptoAmount;
    private Double cryptoPrice;
}
