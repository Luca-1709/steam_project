package com.steam.ms_tienda.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class CompraResponseDTO {

    private Integer          id;
    private Integer          usuarioId;
    private Integer          juegoId;
    private BigDecimal    precioPagado;
    private LocalDateTime fechaCompra;
    private String        estado;
    private String        metodoPago;

    // Enriquecido desde ms-usuarios
    private String usernameUsuario;
    private String nombreUsuario;

    // Enriquecido desde ms-juegos
    private String tituloJuego;
    private String generoJuego;
    private String desarrolladorJuego;
}
