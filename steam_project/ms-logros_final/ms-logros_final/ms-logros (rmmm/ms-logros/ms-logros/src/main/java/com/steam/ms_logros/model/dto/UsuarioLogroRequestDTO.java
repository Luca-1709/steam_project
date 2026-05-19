package com.steam.ms_logros.model.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioLogroRequestDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    private Integer usuarioId;

    @NotNull(message = "El logroId es obligatorio")
    private Integer logroId;
}