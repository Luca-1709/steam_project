package com.steam.ms_pagos.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PagoRequestDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    private Integer usuarioId;

    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Size(max = 50, message = "El metodo de pago no puede superar 50 caracteres")
    private String metodoPago;

    @Size(max = 255, message = "La descripcion no puede superar 255 caracteres")
    private String descripcion;
}