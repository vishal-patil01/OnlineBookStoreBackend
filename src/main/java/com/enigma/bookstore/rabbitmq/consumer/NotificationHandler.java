package com.enigma.bookstore.rabbitmq.consumer;

import com.enigma.bookstore.dto.EmailTemplateDTO;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.util.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationHandler {

    @Autowired
    IMailService mailService;

    @RabbitListener(queues ="subscribers")
    public void handleSubscriberQueue(EmailTemplateDTO emailTemplate) {
        mailService.sendEmail(emailTemplate.email, emailTemplate.subject, emailTemplate.message, emailTemplate.bookList);
        log.info("Email Sent Successfully To " + emailTemplate.email);
    }
}
