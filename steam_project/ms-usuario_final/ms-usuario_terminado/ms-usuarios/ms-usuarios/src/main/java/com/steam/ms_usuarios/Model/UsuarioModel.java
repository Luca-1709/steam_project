package com.steam.ms_usuarios.Model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Username único en toda la plataforma
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    // Email único por usuario
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(length = 60)
    private String pais;

    // Saldo disponible para compras en la tienda
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    // Si el usuario está activo o fue dado de baja
    @Column(nullable = false)
    private Boolean activo = true;
}
