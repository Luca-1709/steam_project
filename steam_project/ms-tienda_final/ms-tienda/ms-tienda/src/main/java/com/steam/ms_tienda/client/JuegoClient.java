package com.steam.ms_tienda.client;
import com.steam.ms_tienda.model.dto.JuegoClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-juegos", url = "${ms.juegos.url}")
public interface JuegoClient {

    @GetMapping("/api/v1/juegos/{id}")
    JuegoClientDTO obtenerJuegoPorId(@PathVariable("id") Integer id);

    // Descuenta 1 unidad de stock al completar la compra
    @PatchMapping("/api/v1/juegos/{id}/stock")
    JuegoClientDTO descontarStock(
            @PathVariable("id") Integer id,
            @RequestParam("cantidad") Integer cantidad);
}