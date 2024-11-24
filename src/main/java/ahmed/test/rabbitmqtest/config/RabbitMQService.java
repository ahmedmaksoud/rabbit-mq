package ahmed.test.rabbitmqtest.config;

import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService implements DisposableBean {

    private final Channel channel;

    public RabbitMQService(Channel channel) {
        this.channel = channel;
    }

    public void sendMessage(String queueName, String message) throws Exception {
        try {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicPublish("", queueName, null, message.getBytes());
            System.out.println("Sent: " + message);
        } finally {
            // Do not close channel here if it’s reused
        }
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy is here");
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
    }
}
