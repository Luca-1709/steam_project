package com.steam.ms_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("ms-usuarios", r -> r
                .path("/api/v1/usuarios/**")
                .uri("http://localhost:8081"))
            .route("ms-juegos", r -> r
                .path("/api/v1/juegos/**")
                .uri("http://localhost:8082"))
            .route("ms-biblioteca", r -> r
                .path("/api/v1/biblioteca/**")
                .uri("http://localhost:8083"))
            .route("ms-tienda", r -> r
                .path("/api/v1/compras/**")
                .uri("http://localhost:8084"))
            .route("ms-resenas", r -> r
                .path("/api/v1/resenas/**")
                .uri("http://localhost:8085"))
            .route("ms-logros", r -> r
                .path("/api/v1/logros/**")
                .uri("http://localhost:8086"))
            .route("ms-amigos", r -> r
                .path("/api/v1/amigos/**")
                .uri("http://localhost:8087"))
            .route("ms-pagos", r -> r
                .path("/api/v1/pagos/**")
                .uri("http://localhost:8088"))
            .route("ms-categorias", r -> r
                .path("/api/v1/categorias/**")
                .uri("http://localhost:8089"))
            .route("ms-desarrolladores", r -> r
                .path("/api/v1/desarrolladores/**")
                .uri("http://localhost:8090"))
            .build();
    }
}