package com.steam.ms_tienda.client;
import com.steam.ms_tienda.model.dto.BibliotecaClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-biblioteca", url = "${ms.biblioteca.url}")
public interface BibliotecaClient {

    @PostMapping("/api/v1/biblioteca")
    Object agregarABiblioteca(@RequestBody BibliotecaClientDTO dto);
}
