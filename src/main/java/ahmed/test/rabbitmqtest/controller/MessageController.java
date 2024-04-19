package ahmed.test.rabbitmqtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ahmed.test.rabbitmqtest.config.RabbitMqTopicProducer;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private RabbitMqTopicProducer rabbitMqTopicProducer;

    @PostMapping("/send")
    public String sendMessageToTopic(@RequestBody String message) {
        rabbitMqTopicProducer.sendMessageToTopic("notifications-topic", "order.created.#", message);
        return "Message sent to RabbitMQ topic";
    }
}