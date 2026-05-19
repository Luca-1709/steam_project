package com.steam.ms_resenas.model.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BibliotecaClientDTO {
    private Integer      id;
    private Integer      usuarioId;
    private Integer      juegoId;
    private LocalDate fechaAdquisicion;
    private Integer   horasJugadas;
    private Boolean   activo;
}
