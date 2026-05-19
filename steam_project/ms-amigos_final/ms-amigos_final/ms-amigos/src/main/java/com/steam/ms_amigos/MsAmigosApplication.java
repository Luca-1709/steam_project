package com.steam.ms_amigos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class MsAmigosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAmigosApplication.class, args);
	}

}
