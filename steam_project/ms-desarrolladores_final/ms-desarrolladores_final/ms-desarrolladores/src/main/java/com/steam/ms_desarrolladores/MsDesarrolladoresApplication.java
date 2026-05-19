package com.steam.ms_desarrolladores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsDesarrolladoresApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsDesarrolladoresApplication.class, args);
    }
}