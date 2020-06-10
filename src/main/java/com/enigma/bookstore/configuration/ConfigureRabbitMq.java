package com.enigma.bookstore.configuration;


import com.enigma.bookstore.properties.ApplicationProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureRabbitMq {
    @Autowired
    ApplicationProperties applicationProperties;

    @Bean
    Queue createQueue() {
        return new Queue(applicationProperties.getRabbitMQQueueName(), true, false, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(applicationProperties.getRabbitMQExchangeName());
    }

    @Bean
    Binding binding(Queue q, TopicExchange exchange) {
        return BindingBuilder.bind(q).to(exchange).with(applicationProperties.getRabbitMQRoutingKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

