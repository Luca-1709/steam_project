package com.steam.ms_amigos.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "amistad",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_amistad",
        columnNames = {"usuario_id", "amigo_id"}
    )
)

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Amistad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Usuario que envia la solicitud
    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    // Usuario que recibe la solicitud
    @Column(name = "amigo_id", nullable = false)
    private Integer amigoId;

    @Column(name = "fecha_amistad", nullable = false)
    private LocalDateTime fechaAmistad;

    // Estado: PENDIENTE, ACEPTADA, RECHAZADA
    @Column(nullable = false, length = 20)
    private String estado;
}
