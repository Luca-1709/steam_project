package com.steam.ms_amigos.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AmistadResponseDTO {

    private Integer       id;
    private Integer       usuarioId;
    private Integer       amigoId;
    private LocalDateTime fechaAmistad;
    private String        estado;
    private String usernameUsuario;
    private String nombreUsuario;
    private String usernameAmigo;
    private String nombreAmigo;
}
