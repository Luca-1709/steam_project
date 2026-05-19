package com.steam.ms_logros.model.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LogroRequestDTO{

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 255, message = "La descripcion no puede superar 255 caracteres")
    private String descripcion;

    @NotNull(message = "El juegoId es obligatorio")
    private Integer juegoId;

    @NotBlank(message = "La condicion es obligatoria")
    private String condicion;
}
