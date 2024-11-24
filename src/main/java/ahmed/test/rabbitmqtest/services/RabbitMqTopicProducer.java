package ahmed.test.rabbitmqtest.services;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.AMQP;

import java.util.Map;

@Component
public class RabbitMqTopicProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static int sequenceNumber = 0;

    @Autowired
    RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    public void sendMessageToTopic(String exchange, String routingKey, Object message) {
//    	AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
//                .expiration("60000")
//
//                .build();
    	
        amqpTemplate.convertAndSend(exchange, routingKey, message);
    }

    public void sendMessageTransaction(String message){
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()

                .contentType("application/json")
                .deliveryMode(2) // Persistent message
                .headers(Map.of(
                        "transactionId", 1111,
                        "sequenceNumber", sequenceNumber++
                ))

                .build();


        MessagePostProcessor messagePostProcessor = proces -> {
            proces.getMessageProperties().setContentType("application/json");
            proces.getMessageProperties().setHeader("transactionId", 1111);
            proces.getMessageProperties().setHeader("sequenceNumber", sequenceNumber++);
            proces.getMessageProperties().setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT);
            return proces;
        };


        amqpTemplate.convertAndSend("transaction-created", "trn.created.#", "please not trn has been created!!  " + message, messagePostProcessor);
    }


    public void sendMessageTransaction2ed(String message){
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()

                .contentType("application/json")
                .deliveryMode(2) // Persistent message
                .headers(Map.of(
                        "transactionId", 1111,
                        "sequenceNumber", sequenceNumber++
                ))

                .build();



       // amqpTemplate.p
        //channel.basicPublish("", "transaction-created", properties, messageBody.getBytes("UTF-8"));

    }


    public void sendThread(String message, String transactionId, int sequenceNumber){

        final int sequenceNumberVar = sequenceNumber;
        MessagePostProcessor messagePostProcessor = proces -> {
            proces.getMessageProperties().setContentType("application/json");
            proces.getMessageProperties().setHeader("transactionId", transactionId);
            proces.getMessageProperties().setHeader("sequenceNumber", sequenceNumberVar);
            proces.getMessageProperties().setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT);
            return proces;
        };


        amqpTemplate.convertAndSend("transaction-created", "trn.created.#", "please not trn has been created!!  " + message, messagePostProcessor);

    }

    public void sendMessageTransactionInThreads(String message){
        for (int i = 0; i < 2000; i++) {
            var trnId = i;
            Thread t = Thread.startVirtualThread(() ->  sendThread("Trans " + trnId + " has been created " + Thread.currentThread(), trnId + "", trnId));
        }

        for (int i = 0; i < 2000; i++) {
            var trnId = i;
            Thread t = Thread.startVirtualThread(() ->  sendThread("Trans " + trnId + " has been created " + Thread.currentThread(), trnId + "", trnId));
        }

        rabbitListenerEndpointRegistry.stop();
    }


}
