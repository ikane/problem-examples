package matthewh86;

import static matthewh86.RabbitConfiguration.ROUTING_KEY;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EventListener {

    @RabbitListener(id = "eventHandler", queues = ROUTING_KEY)
    public void handle(Message message) {
        throw new AmqpRejectAndDontRequeueException("Woops.");
    }
}
