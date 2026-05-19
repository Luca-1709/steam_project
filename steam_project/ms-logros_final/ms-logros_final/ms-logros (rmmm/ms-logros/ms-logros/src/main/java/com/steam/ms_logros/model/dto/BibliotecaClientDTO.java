package com.steam.ms_logros.model.dto;

import lombok.Data;

@Data
public class BibliotecaClientDTO {
    private Integer id;
    private Integer usuarioId;
    private Integer juegoId;
    private Integer horasJugadas;
    private Boolean activo;
}