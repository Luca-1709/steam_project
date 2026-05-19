package com.steam.ms_biblioteca.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BibliotecaResponseDTO {

    private Integer id;
    private Integer   usuarioId;
    private Integer   juegoId;
    private LocalDate fechaAdquisicion;
    private Integer   horasJugadas;
    private Boolean   activo;

    private String usernameUsuario;
    private String nombreUsuario;

    private String     tituloJuego;
    private String     generoJuego;
    private BigDecimal precioJuego;
    private String     desarrolladorJuego;
}