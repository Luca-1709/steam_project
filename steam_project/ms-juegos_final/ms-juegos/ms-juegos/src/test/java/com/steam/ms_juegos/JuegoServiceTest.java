package com.steam.ms_juegos;


import com.steam.ms_juegos.Model.Juego;
import com.steam.ms_juegos.Model.dto.JuegoResponseDTO;
import com.steam.ms_juegos.Repository.JuegoRepository;
import com.steam.ms_juegos.Service.JuegoService;
import com.steam.ms_juegos.exception.RecursoNoEncontradoException;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
        juegoMock.setTitulo(faker.name().title());
        juegoMock.setGenero("RPG");
        juegoMock.setDesarrollador("DarkStudio");
        juegoMock.setPrecio(BigDecimal.valueOf(29990));
        juegoMock.setStock(50);
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
    @DisplayName("Debe verificar que el juego está activo")
    void testJuegoActivo() {
        when(juegoRepository.findById(1)).thenReturn(Optional.of(juegoMock));

        JuegoResponseDTO resultado = juegoService.findById(1);

        assertTrue(resultado.getActivo());
    }
}