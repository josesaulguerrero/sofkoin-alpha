package co.com.sofkoin.alpha.infrastructure.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    public static final String EXCHANGE = "exchange.main";

    public static final String MAIN_QUEUE = "domain_events.main";

    public static final String MAIN_ROUTING_KEY = "routing_key.main";

    @Bean
    public Queue generalQueue() {
        return new Queue(MAIN_QUEUE);
    }

    @Bean
    public TopicExchange getTopicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding BindingToGeneralQueue() {
        return BindingBuilder
                .bind(generalQueue())
                .to(getTopicExchange())
                .with(MAIN_ROUTING_KEY);
    }
}
