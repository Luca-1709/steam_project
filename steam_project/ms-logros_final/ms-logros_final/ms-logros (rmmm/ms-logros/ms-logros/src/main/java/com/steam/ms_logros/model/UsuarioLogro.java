package com.steam.ms_logros.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "usuario_logro",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_usuario_logro",
        columnNames = {"usuario_id", "logro_id"}
    )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLogro{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    @Column(name = "logro_id", nullable = false)
    private Integer logroId;

    @Column(name = "fecha_logro", nullable = false)
    private LocalDateTime fechaLogro;
}
