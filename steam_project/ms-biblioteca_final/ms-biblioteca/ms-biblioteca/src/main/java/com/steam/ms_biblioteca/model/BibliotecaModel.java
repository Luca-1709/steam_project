package com.steam.ms_biblioteca.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "entrada_biblioteca",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_usuario_juego",
        columnNames = {"usuario_id", "juego_id"}
    )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BibliotecaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    @Column(name = "juego_id", nullable = false)
    private Integer juegoId;

    @Column(name = "fecha_adquisicion", nullable = false)
    private LocalDate fechaAdquisicion;

    @Column(name = "horas_jugadas", nullable = false)
    private Integer horasJugadas = 0;

    @Column(nullable = false)
    private Boolean activo = true;
}

