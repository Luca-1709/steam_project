package com.steam.ms_juegos.Model.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class JuegoResponseDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private BigDecimal precio;
    private LocalDate fechaLanzamiento;
    private String desarrollador;
    private String genero;
    private Integer stock;
    private Boolean activo;
}
