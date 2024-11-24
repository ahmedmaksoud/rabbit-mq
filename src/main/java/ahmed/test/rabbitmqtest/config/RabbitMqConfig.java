package ahmed.test.rabbitmqtest.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;


@Configuration
public class RabbitMqConfig {



    //    @Bean
//    public TopicExchange topicExchange() {
//        return new TopicExchange("notifications-topic");
//    }

    private Connection connection;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("localhost"); // Adjust to your RabbitMQ host
        connectionFactory.setPort(5672);        // Default RabbitMQ port
        connectionFactory.setUsername("guest"); // Replace with your username
        connectionFactory.setPassword("guest"); // Replace with your password
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory cachingConnectionFactory) {
        System.out.println("RabbitTemplate>>>>>>>>>>>>>>>>>>>>>>>>");
        return new RabbitTemplate(cachingConnectionFactory);
    }
    @Bean
    public Channel rabbitChannel(ConnectionFactory connectionFactory) throws Exception {
        if (connection == null) {
            connection = connectionFactory.createConnection().getDelegate();
        }
        return connection.createChannel();
    }

}