package com.steam.ms_tienda.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class JuegoClientDTO {
    private Integer       id;
    private String     titulo;
    private BigDecimal precio;
    private String     genero;
    private String     desarrollador;
    private Integer    stock;
    private Boolean    activo;
}