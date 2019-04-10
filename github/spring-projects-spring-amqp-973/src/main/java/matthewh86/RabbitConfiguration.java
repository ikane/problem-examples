package matthewh86;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
@EnableRabbit
public class RabbitConfiguration {

    public static final String ROUTING_KEY = "routingKey";
    public static final String EXCHANGE_NAME = "exchangeName";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean(ROUTING_KEY)
    Queue applicationEventQueue() {
        return new Queue(ROUTING_KEY);
    }

    @Bean
    Binding applicationEventBinding(final TopicExchange exchange, @Qualifier(ROUTING_KEY) final Queue queue) {
        return binding(exchange, queue);
    }

    private Binding binding(TopicExchange exchange, Queue queue) {
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(ROUTING_KEY);
    }

}
