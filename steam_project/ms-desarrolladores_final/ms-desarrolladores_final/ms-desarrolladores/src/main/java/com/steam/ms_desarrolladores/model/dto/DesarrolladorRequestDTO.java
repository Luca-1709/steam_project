package com.steam.ms_desarrolladores.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DesarrolladorRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @NotBlank(message = "El pais es obligatorio")
    @Size(max = 60, message = "El pais no puede superar 60 caracteres")
    private String pais;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 255, message = "La descripcion no puede superar 255 caracteres")
    private String descripcion;

    @Size(max = 150, message = "El sitio web no puede superar 150 caracteres")
    private String sitioWeb;
}