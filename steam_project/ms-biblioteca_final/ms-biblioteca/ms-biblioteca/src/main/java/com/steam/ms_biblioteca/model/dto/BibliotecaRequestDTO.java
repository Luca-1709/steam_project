package com.steam.ms_biblioteca.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BibliotecaRequestDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    @Positive(message = "El usuarioId debe ser un número positivo")
    private Integer usuarioId;

    @NotNull(message = "El juegoId es obligatorio")
    @Positive(message = "El juegoId debe ser un número positivo")
    private Integer juegoId;

    @Min(value = 0, message = "Las horas jugadas no pueden ser negativas")
    private Integer horasJugadas = 0;
}
