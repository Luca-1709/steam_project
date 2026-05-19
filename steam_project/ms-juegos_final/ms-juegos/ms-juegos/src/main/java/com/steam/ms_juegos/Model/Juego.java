package com.steam.ms_juegos.Model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "juego")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Juego {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "fecha_lanzamiento")
    private LocalDate fechaLanzamiento;

    @Column(length = 100)
    private String desarrollador;

    @Column(length = 80)
    private String genero;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false)
    private Boolean activo = true;
}
