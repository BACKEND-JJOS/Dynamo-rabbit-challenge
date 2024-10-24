package com.ias.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

public interface EventRabbitConfig {
    TopicExchange exchange();

    Queue queue();

    Binding binding();

}
