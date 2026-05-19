package com.steam.ms_resenas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "resena",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_usuario_juego",
        columnNames = {"usuario_id", "juego_id"}
    )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    @Column(name = "juego_id", nullable = false)
    private Integer juegoId;

    @Column(nullable = false)
    private Integer puntuacion;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private Boolean activo = true;
}

