package com.ias;

import com.ias.config.UserEventRabbitConfig;
import com.ias.gateway.RabbitEventsGatewayImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitEventsGatewayImplTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitEventsGatewayImpl rabbitEventsGateway;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("1", "User One", "ACTIVE");
    }

    @Test
    void publishUserUpdatedEvent_shouldSendMessageToRabbit() {
        Mono<Void> result = rabbitEventsGateway.publishUserUpdatedEvent(user);
        result.block();

        verify(rabbitTemplate).convertAndSend(
                UserEventRabbitConfig.USER_EVENTS_EXCHANGE,
                UserEventRabbitConfig.USER_UPDATED_ROUTING_KEY,
                user
        );
    }
}

