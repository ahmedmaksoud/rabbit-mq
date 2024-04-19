package ahmed.test.rabbitmqtest.config;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.AMQP;

@Component
public class RabbitMqTopicProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessageToTopic(String exchange, String routingKey, Object message) {
    	AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .expiration("60000")
                .build();
    	
        amqpTemplate.convertAndSend(exchange, routingKey, message);
    }
}
