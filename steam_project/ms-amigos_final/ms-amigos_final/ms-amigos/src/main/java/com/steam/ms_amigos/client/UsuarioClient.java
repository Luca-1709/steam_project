package com.steam.ms_amigos.client;

import com.steam.ms_amigos.client.dto.UsuarioClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-usuarios", url = "${ms.usuarios.url}")
public interface UsuarioClient {

    @GetMapping("/api/v1/usuarios/{id}")
    UsuarioClientDTO obtenerUsuarioPorId(@PathVariable("id") Integer id);
}
