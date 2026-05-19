package com.steam.ms_logros.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "logro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logro{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(name = "juego_id", nullable = false)
    private Integer juegoId;

    @Column(nullable = false, length = 100)
    private String condicion;
}