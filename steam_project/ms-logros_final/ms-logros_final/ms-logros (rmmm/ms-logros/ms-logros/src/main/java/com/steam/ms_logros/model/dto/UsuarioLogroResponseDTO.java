package com.steam.ms_logros.model.dto;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioLogroResponseDTO {
    private Integer       id;
    private Integer       usuarioId;
    private Integer       logroId;
    private LocalDateTime fechaLogro;

    private String usernameUsuario;
    private String nombreUsuario;

    private String nombreLogro;
    private String descripcionLogro;
    private String tituloJuego;
}