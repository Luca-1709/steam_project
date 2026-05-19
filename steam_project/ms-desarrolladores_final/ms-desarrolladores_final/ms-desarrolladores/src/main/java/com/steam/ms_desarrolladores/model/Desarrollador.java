package com.steam.ms_desarrolladores.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "desarrollador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Desarrollador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(nullable = false, length = 60)
    private String pais;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(name = "sitio_web", length = 150)
    private String sitioWeb;

    @Column(nullable = false)
    private Boolean activo = true;
}