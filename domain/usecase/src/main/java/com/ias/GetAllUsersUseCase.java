package com.ias;

import com.ias.gateway.EventsGateway;
import com.ias.gateway.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase {

    private final UserRepository userRepository;
    private final EventsGateway eventsGateway;

    public Flux<User> get() {
        return userRepository.getAll()
                .filter(user -> user.getStatus().equals(Status.INACTIVE.toString()))
                .flatMap(user -> {
                    user.setStatus(Status.ACTIVE.toString());
                    return userRepository.save(user)
                            .flatMap(updatedUser -> eventsGateway.publishUserUpdatedEvent(updatedUser)
                                    .thenReturn(updatedUser)
                            );
                });
    }
}
