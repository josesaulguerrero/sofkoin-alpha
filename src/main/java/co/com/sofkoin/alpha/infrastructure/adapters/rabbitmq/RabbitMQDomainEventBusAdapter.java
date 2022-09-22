package co.com.sofkoin.alpha.infrastructure.adapters.rabbitmq;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.infrastructure.commons.json.JSONMapper;
import co.com.sofkoin.alpha.infrastructure.config.rabbitmq.RabbitMQConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RabbitMQDomainEventBusAdapter implements DomainEventBus {
    private final JSONMapper jsonMapper;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishEvent(DomainEvent event) {
        RabbitMQMessage message = new RabbitMQMessage(
                event.getClass().getName(),
                this.jsonMapper.writeToJson(event)
        );
        this.rabbitTemplate.convertAndSend(
                RabbitMQConfiguration.EXCHANGE,
                RabbitMQConfiguration.MAIN_ROUTING_KEY,
                message.serialize()
        );
    }
}
