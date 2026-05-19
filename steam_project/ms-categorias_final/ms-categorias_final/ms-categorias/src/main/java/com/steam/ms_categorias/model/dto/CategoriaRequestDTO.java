package com.steam.ms_categorias.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CategoriaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
    private String nombre;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 255, message = "La descripcion no puede superar 255 caracteres")
    private String descripcion;
}