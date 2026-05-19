package com.steam.ms_desarrolladores.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class DesarrolladorResponseDTO {

    private Integer id;
    private String  nombre;
    private String  pais;
    private String  descripcion;
    private String  sitioWeb;
    private Boolean activo;

    // Juegos del desarrollador enriquecidos desde ms-juegos
    private List<JuegoClientResponseDTO> juegos;
}