package com.steam.ms_logros.model.dto;

import lombok.Data;

@Data
public class LogroResponseDTO {
    private Integer id;
    private String  nombre;
    private String  descripcion;
    private Integer juegoId;
    private String  condicion;
    private String tituloJuego;
    private String generoJuego;
}