package com.steam.ms_logros.model.dto;

import lombok.Data;

@Data
public class JuegoClientDTO {
    private Integer id;
    private String  titulo;
    private String  genero;
    private Boolean activo;
}