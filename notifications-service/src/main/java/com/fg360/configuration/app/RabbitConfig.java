package com.fg360.configuration.app;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchange;

    @Value("${spring.rabbitmq.routing.alert.created.key}")
    private String alertRoutingKey;

    @Value("${spring.rabbitmq.queue.alert.created.name}")
    private String queueAlertCreated;

    // Crea la cola, con true es durable
    @Bean
    public Queue notificationQueue() {
        return new Queue(queueAlertCreated, true);
    }


    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding emailBinding(Queue emailNotificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(emailNotificationQueue)
                .to(notificationExchange)
                .with(alertRoutingKey);
    }

    @Bean
    public Binding pushBinding(Queue pushNotificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(pushNotificationQueue)
                .to(notificationExchange)
                .with(alertRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
