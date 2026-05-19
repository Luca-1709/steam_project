package com.steam.ms_resenas.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResenaResponseDTO {

    private Integer          id;
    private Integer          usuarioId;
    private Integer          juegoId;
    private Integer       puntuacion;
    private String        titulo;
    private String        contenido;
    private LocalDateTime fecha;
    private Boolean       activo;

    private String usernameUsuario;
    private String nombreUsuario;

    private String tituloJuego;
    private String generoJuego;
    private String desarrolladorJuego;
}