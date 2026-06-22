package com.steam.ms_biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class MsBibliotecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsBibliotecaApplication.class, args);
	}

}
