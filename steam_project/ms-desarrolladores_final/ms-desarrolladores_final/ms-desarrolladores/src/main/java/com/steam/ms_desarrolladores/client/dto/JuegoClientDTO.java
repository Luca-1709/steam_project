package com.steam.ms_desarrolladores.client.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class JuegoClientDTO {
    private Integer    id;
    private String     titulo;
    private String     genero;
    private BigDecimal precio;
    private String     desarrollador;
    private Integer    stock;
    private Boolean    activo;
}