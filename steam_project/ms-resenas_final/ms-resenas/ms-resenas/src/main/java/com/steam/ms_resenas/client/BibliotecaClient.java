package com.steam.ms_resenas.client;

import com.steam.ms_resenas.model.dto.BibliotecaClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "ms-biblioteca", url = "${ms.biblioteca.url}")
public interface BibliotecaClient {

    @GetMapping("/api/v1/biblioteca/usuario/{usuarioId}")
    List<BibliotecaClientDTO> obtenerBibliotecaDeUsuario(
            @PathVariable("usuarioId") Integer usuarioId);
}
