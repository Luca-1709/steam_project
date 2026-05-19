package com.steam.ms_pagos.client;

import com.steam.ms_pagos.client.dto.UsuarioClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;

@FeignClient(name = "ms-usuarios", url = "${ms.usuarios.url}")
public interface UsuarioClient {

    @GetMapping("/api/v1/usuarios/{id}")
    UsuarioClientDTO obtenerUsuarioPorId(@PathVariable("id") Integer id);

    @PatchMapping("/api/v1/usuarios/{id}/saldo")
    UsuarioClientDTO agregarSaldo(
            @PathVariable("id") Integer id,
            @RequestParam("monto") BigDecimal monto);
}