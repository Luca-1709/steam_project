package com.steam.ms_resenas.client;

import com.steam.ms_resenas.model.dto.JuegoClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-juegos", url = "${ms.juegos.url}")
public interface JuegoClient {

    @GetMapping("/api/v1/juegos/{id}")
    JuegoClientDTO obtenerJuegoPorId(@PathVariable("id") Integer id);
}