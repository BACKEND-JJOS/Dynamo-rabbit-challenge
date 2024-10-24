package com.ias;


import com.ias.response.UserResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GetAllUsersUseCase getAllUsersUseCase;

    @Test
    void shouldGetAllUsers_shouldReturnOk_whenUsersHasStatusInactive() {
        User user1 = User.builder()
                .id("any-uuid-1")
                .name("user1-name")
                .status(Status.ACTIVE.toString())
                .build();

        User user2 = User.builder()
                .id("any-uuid-2")
                .name("user2-name")
                .status(Status.ACTIVE.toString())
                .build();

        given(getAllUsersUseCase.get())
                .willReturn(Flux.just(user1, user2));

        webTestClient.get()
                .uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(2)
                .value(userResponses -> {
                    Assertions.assertThat(userResponses.get(0).getName()).isEqualTo(user1.getName());
                    Assertions.assertThat(userResponses.get(0).getStatus()).isEqualTo(user1.getStatus());
                });

        Mockito.verify(getAllUsersUseCase).get();

    }
}
