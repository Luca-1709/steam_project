package com.steam.ms_categorias.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class CategoriaResponseDTO {

    private Integer id;
    private String  nombre;
    private String  descripcion;
    private Boolean activo;

    private List<JuegoClientResponseDTO> juegos;
}