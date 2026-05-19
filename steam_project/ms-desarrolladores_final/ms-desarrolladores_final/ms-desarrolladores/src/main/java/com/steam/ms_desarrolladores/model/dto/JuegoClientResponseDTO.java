package com.steam.ms_desarrolladores.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class JuegoClientResponseDTO {
    private Integer    id;
    private String     titulo;
    private String     genero;
    private BigDecimal precio;
    private Integer    stock;
    private Boolean    activo;
}