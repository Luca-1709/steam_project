package com.steam.ms_biblioteca.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UsuarioClientDTO {
    private Integer      id;
    private String    username;
    private String    nombre;
    private String    apellido;
    private String    pais;
    private BigDecimal saldo;
    private LocalDate fechaRegistro;
    private Boolean   activo;
}
