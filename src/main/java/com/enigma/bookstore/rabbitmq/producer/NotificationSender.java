package com.enigma.bookstore.rabbitmq.producer;

import com.enigma.bookstore.dto.EmailTemplateDTO;
import com.enigma.bookstore.properties.ApplicationProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    ApplicationProperties applicationProperties;

    public void addToSubscriberQueue(EmailTemplateDTO emailTemplate) {
        rabbitTemplate.convertAndSend(applicationProperties.getRabbitMQExchangeName(), applicationProperties.getRabbitMQRoutingKey(), emailTemplate);
    }
}
