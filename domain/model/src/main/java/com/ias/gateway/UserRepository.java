package com.ias.gateway;


import com.ias.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Flux<User> getAll();

    Mono<User> save(User user);
}
