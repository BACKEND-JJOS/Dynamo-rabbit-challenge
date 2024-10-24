package com.ias;

import com.google.gson.Gson;
import com.ias.gateway.UserGatewayImpl;
import com.ias.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserGatewayImplTest {

    @Mock
    private DynamoReactiveUserAdapter dynamoReactiveUserAdapter;

    @Mock
    private Gson mapper;

    private static final String USER_JSON = "{\"id\":\"1\",\"name\":\"User One\",\"status\":\"ACTIVE\"}";

    @InjectMocks
    private UserGatewayImpl userGateway;

    @Test
    void getAll_shouldReturnFluxOfUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("1");
        userEntity.setName("User One");
        userEntity.setStatus("ACTIVE");

        when(dynamoReactiveUserAdapter.getAll()).thenReturn(Flux.just(userEntity));
        when(mapper.toJson(userEntity)).thenReturn(USER_JSON);
        when(mapper.fromJson(any(String.class), eq(User.class))).thenReturn(new User("1", "User One", "ACTIVE"));

        Flux<User> result = userGateway.getAll();

        assertThat(result.collectList().block()).hasSize(1);
        verify(dynamoReactiveUserAdapter).getAll();
        verify(mapper).fromJson(anyString(), eq(User.class));
    }

    @Test
    void save_shouldReturnSavedUser() {
        User user = new User("1", "User One", "ACTIVE");
        UserEntity userEntity = new UserEntity();
        userEntity.setId("1");
        userEntity.setName("User One");
        userEntity.setStatus("ACTIVE");

        when(mapper.toJson(user)).thenReturn(USER_JSON);
        when(mapper.fromJson(USER_JSON, UserEntity.class))
                .thenReturn(userEntity);

        when(dynamoReactiveUserAdapter.save(userEntity)).thenReturn(Mono.just(userEntity));

        when(mapper.toJson(userEntity)).thenReturn(USER_JSON);
        when(mapper.fromJson(USER_JSON, User.class))
                .thenReturn(user);

        Mono<User> result = userGateway.save(user);

        assertThat(result.block()).isEqualTo(user);
        verify(dynamoReactiveUserAdapter).save(userEntity);
        verify(mapper).toJson(user);
        verify(mapper).fromJson("{\"id\":\"1\",\"name\":\"User One\",\"status\":\"ACTIVE\"}", UserEntity.class);
        verify(mapper).fromJson(any(String.class), eq(User.class));
    }
}
