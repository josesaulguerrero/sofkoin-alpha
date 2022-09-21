package co.com.sofkoin.alpha.domain.user.commands;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SendOfferMessage {
    private String senderId;
    private String receiverId;
    private String cryptoSymbol;
    private Double cryptoAmount;
    private Double cryptoPrice;
}
