package com.steam.ms_pagos.client.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UsuarioClientDTO {
    private Integer    id;
    private String     username;
    private String     nombre;
    private String     apellido;
    private BigDecimal saldo;
    private Boolean    activo;
}