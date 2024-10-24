package com.ias;

import com.ias.openapi.OpenApiDoc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;


@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route()
                .GET("user", handler::listenGETUpdatedUsers, OpenApiDoc::getUpdatedUsers)
                .build();
    }

}
