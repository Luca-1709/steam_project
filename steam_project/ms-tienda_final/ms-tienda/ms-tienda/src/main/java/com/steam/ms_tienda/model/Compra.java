package com.steam.ms_tienda.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    @Column(name = "juego_id", nullable = false)
    private Integer juegoId;

    @Column(name = "precio_pagado", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPagado;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(name = "metodo_pago", nullable = false, length = 50)
    private String metodoPago;
}