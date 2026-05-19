package com.steam.ms_tienda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsTiendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTiendaApplication.class, args);
	}

}
