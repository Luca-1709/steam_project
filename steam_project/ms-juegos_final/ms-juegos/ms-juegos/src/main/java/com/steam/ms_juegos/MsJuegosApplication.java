package com.steam.ms_juegos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsJuegosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsJuegosApplication.class, args);
	}

}
