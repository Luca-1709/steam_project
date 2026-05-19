package com.steam.ms_pagos.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoResponseDTO {

    private Integer       id;
    private Integer       usuarioId;
    private BigDecimal    monto;
    private String        metodoPago;
    private String        estado;
    private LocalDateTime fechaPago;
    private String        descripcion;
    private String usernameUsuario;
    private String nombreUsuario;
}