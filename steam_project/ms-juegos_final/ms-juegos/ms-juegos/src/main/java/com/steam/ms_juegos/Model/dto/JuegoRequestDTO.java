package com.steam.ms_juegos.Model.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class JuegoRequestDTO {
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede superar 150 caracteres")
    private String titulo;

    @Size(max = 2000, message = "La descripción no puede superar 2000 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    private LocalDate fechaLanzamiento;

    @NotBlank(message = "El desarrollador es obligatorio")
    @Size(max = 100, message = "El nombre del desarrollador no puede superar 100 caracteres")
    private String desarrollador;

    @NotBlank(message = "El género es obligatorio")
    @Size(max = 80, message = "El género no puede superar 80 caracteres")
    private String genero;


    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock = 0;
}
