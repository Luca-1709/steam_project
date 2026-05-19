package com.steam.ms_tienda.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CompraRequestDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    @Positive(message = "El usuarioId debe ser un número positivo")
    private Integer usuarioId;

    @NotNull(message = "El juegoId es obligatorio")
    @Positive(message = "El juegoId debe ser un número positivo")
    private Integer juegoId;

    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 50, message = "El método de pago no puede superar 50 caracteres")
    private String metodoPago;
}
