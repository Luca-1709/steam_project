package com.steam.ms_juegos;

import com.steam.ms_juegos.Model.Juego;
import com.steam.ms_juegos.Model.dto.JuegoRequestDTO;
import com.steam.ms_juegos.Model.dto.JuegoResponseDTO;
import com.steam.ms_juegos.Repository.JuegoRepository;
import com.steam.ms_juegos.Service.JuegoService;
import com.steam.ms_juegos.exception.RecursoNoEncontradoException;
import com.steam.ms_juegos.exception.ReglaDeNegocioException;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JuegoServiceTest {

    @Mock
    private JuegoRepository juegoRepository;

    @InjectMocks
    private JuegoService juegoService;

    private Faker faker;
    private Juego juegoMock;

    @BeforeEach
    void setUp() {
        faker = new Faker();

        juegoMock = new Juego();
        juegoMock.setId(1);
        juegoMock.setTitulo(faker.videoGame().title());
        juegoMock.setDescripcion(faker.lorem().sentence());
        juegoMock.setPrecio(new BigDecimal("19990"));
        juegoMock.setFechaLanzamiento(LocalDate.now());
        juegoMock.setDesarrollador(faker.company().name());
        juegoMock.setGenero("RPG");
        juegoMock.setStock(10);
        juegoMock.setActivo(true);
    }

    @Test
    @DisplayName("Debe retornar juego cuando existe el ID")
    void testFindById_exitoso() {
        when(juegoRepository.findById(1)).thenReturn(Optional.of(juegoMock));

        JuegoResponseDTO resultado = juegoService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(juegoMock.getTitulo(), resultado.getTitulo());
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando el juego no existe")
    void testFindById_noEncontrado() {
        when(juegoRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> juegoService.findById(999));
    }

    @Test
    @DisplayName("Debe lanzar excepcion al crear juego con titulo duplicado")
    void testSave_tituloDuplicado() {
        JuegoRequestDTO dto = new JuegoRequestDTO();
        dto.setTitulo("Hades II");
        when(juegoRepository.existsByTituloIgnoreCase("Hades II")).thenReturn(true);

        assertThrows(ReglaDeNegocioException.class, () -> juegoService.save(dto));
        verify(juegoRepository, never()).save(any(Juego.class));
    }

    @Test
    @DisplayName("Debe descontar stock correctamente")
    void testDescontarStock_exitoso() {
        when(juegoRepository.findById(1)).thenReturn(Optional.of(juegoMock));
        when(juegoRepository.save(any(Juego.class))).thenAnswer(inv -> inv.getArgument(0));

        JuegoResponseDTO resultado = juegoService.descontarStock(1, 3);

        assertEquals(7, resultado.getStock());
    }

    @Test
    @DisplayName("Debe lanzar excepcion si el stock es insuficiente")
    void testDescontarStock_insuficiente() {
        juegoMock.setStock(2);
        when(juegoRepository.findById(1)).thenReturn(Optional.of(juegoMock));

        assertThrows(ReglaDeNegocioException.class,
                () -> juegoService.descontarStock(1, 5));
        verify(juegoRepository, never()).save(any(Juego.class));
    }
}