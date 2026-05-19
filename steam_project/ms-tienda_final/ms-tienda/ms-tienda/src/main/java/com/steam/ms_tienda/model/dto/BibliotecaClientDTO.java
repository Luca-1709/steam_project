package com.steam.ms_tienda.model.dto;
import lombok.Data;

@Data
public class BibliotecaClientDTO {
    private Integer    usuarioId;
    private Integer    juegoId;
    private Integer horasJugadas;
}