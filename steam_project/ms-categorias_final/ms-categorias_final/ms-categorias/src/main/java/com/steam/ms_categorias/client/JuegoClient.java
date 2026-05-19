package com.steam.ms_categorias.client;

import com.steam.ms_categorias.client.dto.JuegoClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "ms-juegos", url = "${ms.juegos.url}")
public interface JuegoClient {

    @GetMapping("/api/v1/juegos/genero/{genero}")
    List<JuegoClientDTO> obtenerJuegosPorGenero(
            @PathVariable("genero") String genero);
}