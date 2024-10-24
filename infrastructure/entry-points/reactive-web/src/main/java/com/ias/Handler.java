package com.ias;

import com.google.gson.Gson;
import com.ias.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final GetAllUsersUseCase getAllUsersUseCase;

    private final Gson mapper;

    public Mono<ServerResponse> listenGETUpdatedUsers(ServerRequest serverRequest) {

        String traceUUID = UUID.randomUUID().toString();
        return getAllUsersUseCase.get()
                .contextWrite(Context.of("traceUUID", traceUUID))
                .doOnSubscribe(subscription -> log.info("Incoming request with trace UUID: {}", traceUUID))
                .map(user -> mapper.fromJson(mapper.toJson(user), UserResponse.class))
                .collectList()
                .flatMap(usersResponse -> ServerResponse.ok().bodyValue(usersResponse));
    }
}
