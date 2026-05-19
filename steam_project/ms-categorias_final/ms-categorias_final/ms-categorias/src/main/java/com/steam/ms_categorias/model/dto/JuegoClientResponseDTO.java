package com.steam.ms_categorias.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class JuegoClientResponseDTO {
    private Integer    id;
    private String     titulo;
    private BigDecimal precio;
    private String     desarrollador;
    private Integer    stock;
    private Boolean    activo;
}