package com.steam.ms_amigos.model.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AmistadRequestDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    private Integer usuarioId;

    @NotNull(message = "El amigoId es obligatorio")
    private Integer amigoId;
}