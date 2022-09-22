package co.com.sofkoin.alpha.infrastructure.adapters.rabbitmq;

import co.com.sofkoin.alpha.infrastructure.commons.json.JSONMapper;
import co.com.sofkoin.alpha.infrastructure.commons.json.JSONMapperImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class RabbitMQMessage {
    private static final JSONMapper jsonMapper = new JSONMapperImpl();
    private final String type;
    private final String body;
    private final Instant instant;


    public RabbitMQMessage(String type, String body) {
        this.type = type;
        this.body = body;
        this.instant = Instant.now();
    }

    public String serialize() {
        return jsonMapper.writeToJson(this);
    }

    public static RabbitMQMessage from(String charSequence) {
        return RabbitMQMessage.deserialize(charSequence);
    }

    public static RabbitMQMessage deserialize(String charSequence) {
        return (RabbitMQMessage) jsonMapper.readFromJson(charSequence, RabbitMQMessage.class);
    }
}
