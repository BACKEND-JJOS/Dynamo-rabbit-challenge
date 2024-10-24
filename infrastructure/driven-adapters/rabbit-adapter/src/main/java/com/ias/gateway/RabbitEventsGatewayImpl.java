package com.ias.gateway;

import com.ias.User;
import com.ias.config.UserEventRabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitEventsGatewayImpl implements EventsGateway {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public Mono<Void> publishUserUpdatedEvent(User user) {
        return Mono.fromRunnable(() -> rabbitTemplate.convertAndSend(
                UserEventRabbitConfig.USER_EVENTS_EXCHANGE,
                UserEventRabbitConfig.USER_UPDATED_ROUTING_KEY,
                user)
        );
    }
}
