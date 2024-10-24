package com.ias;




import com.ias.gateway.EventsGateway;
import com.ias.gateway.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllUsersUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventsGateway eventsGateway;

    @InjectMocks
    private GetAllUsersUseCase getAllUsersUseCase;

    @Test
    void shouldActivateInactiveUsersAndPublishEvents_whenUsersExist() {
        User user1 = User.builder()
                .id("any-uuid-1")
                .name("user1-name")
                .status(Status.INACTIVE.toString())
                .build();

        User user2 = User.builder()
                .id("any-uuid-2")
                .name("user2-name")
                .status(Status.INACTIVE.toString())
                .build();

        given(userRepository.getAll())
                .willReturn(Flux.just(user1, user2));

        given(userRepository.save(any(User.class))).willAnswer(invocation -> {
            User updatedUser = invocation.getArgument(0);
            updatedUser.setStatus(Status.ACTIVE.toString());
            return Mono.just(updatedUser);
        });

        given(eventsGateway.publishUserUpdatedEvent(any(User.class))).willReturn(Mono.empty());

        Flux<User> result = getAllUsersUseCase.get();
        List<User> users = result.collectList().block();

        Assertions.assertThat(users).hasSize(2);
        Assertions.assertThat(users.get(0).getStatus()).isEqualTo(Status.ACTIVE.toString());
        Assertions.assertThat(users.get(1).getStatus()).isEqualTo(Status.ACTIVE.toString());

        then(userRepository).should(times(2)).save(any(User.class));
        then(eventsGateway).should(times(2)).publishUserUpdatedEvent(any(User.class));

    }
}
