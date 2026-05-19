package com.steam.ms_resenas.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ResenaRequestDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    private Integer usuarioId;

    @NotNull(message = "El juegoId es obligatorio")
    private Integer juegoId;

    @NotNull(message = "La puntuacion es obligatoria")
    private Integer puntuacion;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 150, message = "El titulo no puede superar 150 caracteres")
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    @Size(min = 10, max = 2000, message = "El contenido debe tener entre 10 y 2000 caracteres")
    private String contenido;
}
