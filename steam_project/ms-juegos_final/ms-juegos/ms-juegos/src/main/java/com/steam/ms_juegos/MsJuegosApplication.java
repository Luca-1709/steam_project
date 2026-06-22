package com.steam.ms_juegos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class MsJuegosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsJuegosApplication.class, args);
	}

}
