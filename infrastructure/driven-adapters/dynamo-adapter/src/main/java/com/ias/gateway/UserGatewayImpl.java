package com.ias.gateway;

import com.google.gson.Gson;
import com.ias.DynamoReactiveUserAdapter;
import com.ias.User;
import com.ias.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserGatewayImpl implements UserRepository {

    private final DynamoReactiveUserAdapter dynamoReactiveUserAdapter;

    private final Gson mapper;

    @Override
    public Flux<User> getAll() {
        return dynamoReactiveUserAdapter.getAll()
                .map(userEntity -> mapper.fromJson(mapper.toJson(userEntity), User.class));
    }

    @Override
    public Mono<User> save(User user) {
        UserEntity userEntity = mapper.fromJson(mapper.toJson(user), UserEntity.class);
        return dynamoReactiveUserAdapter.save(userEntity)
                .map(userEntitySaved -> mapper.fromJson(mapper.toJson(userEntitySaved), User.class));
    }
}
