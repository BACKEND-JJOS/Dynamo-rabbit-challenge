package com.ias.gateway;

import com.ias.User;
import reactor.core.publisher.Mono;

public interface EventsGateway {
    Mono<Void> publishUserUpdatedEvent(User user);
}
