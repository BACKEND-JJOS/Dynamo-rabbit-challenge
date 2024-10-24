package com.ias.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserEventRabbitConfig implements EventRabbitConfig {
    public static final String USER_EVENTS_EXCHANGE = "user.events";
    public static final String USER_UPDATED_QUEUE = "user.updated.notifications.queue";
    public static final String USER_UPDATED_ROUTING_KEY = "user.updated";

    @Bean
    @Override
    public TopicExchange exchange() {
        return new TopicExchange(USER_EVENTS_EXCHANGE, true, false);
    }

    @Bean
    @Override
    public Queue queue() {
        return new Queue(USER_UPDATED_QUEUE, true);
    }

    @Bean
    @Override
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(USER_UPDATED_ROUTING_KEY);
    }
}
