package com.steam.ms_logros;

import com.steam.ms_logros.client.JuegoClient;
import com.steam.ms_logros.exception.RecursoNoEncontradoException;
import com.steam.ms_logros.model.Logro;
import com.steam.ms_logros.model.dto.JuegoClientDTO;
import com.steam.ms_logros.model.dto.LogroRequestDTO;
import com.steam.ms_logros.model.dto.LogroResponseDTO;
import com.steam.ms_logros.repository.LogroRepository;
import com.steam.ms_logros.service.LogroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogroServiceTest {

    @Mock private LogroRepository logroRepository;
    @Mock private JuegoClient juegoClient;

    @InjectMocks private LogroService logroService;

    private Logro logro;
    private JuegoClientDTO juego;

    @BeforeEach
    void setUp() {
        logro = new Logro();
        logro.setId(1);
        logro.setNombre("Primera victoria");
        logro.setDescripcion("Gana tu primera partida");
        logro.setJuegoId(2);
        logro.setCondicion("Ganar 1 partida");

        juego = new JuegoClientDTO();
        juego.setId(2);
        juego.setTitulo("Hades II");
        juego.setGenero("Roguelike");
        juego.setActivo(true);
    }

    @Test
    @DisplayName("findLogroById: retorna el logro enriquecido cuando existe")
    void testFindLogroById_exitoso() {
        when(logroRepository.findById(1)).thenReturn(Optional.of(logro));
        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juego);

        LogroResponseDTO dto = logroService.findLogroById(1);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Hades II", dto.getTituloJuego());
    }

    @Test
    @DisplayName("findLogroById: lanza RecursoNoEncontradoException cuando no existe")
    void testFindLogroById_noEncontrado() {
        when(logroRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> logroService.findLogroById(999));
    }

    @Test
    @DisplayName("saveLogro: crea el logro validando el juego")
    void testSaveLogro_exitoso() {
        LogroRequestDTO request = new LogroRequestDTO();
        request.setNombre("Primera victoria");
        request.setDescripcion("Gana tu primera partida");
        request.setJuegoId(2);
        request.setCondicion("Ganar 1 partida");

        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juego);
        when(logroRepository.save(any(Logro.class))).thenReturn(logro);

        LogroResponseDTO dto = logroService.saveLogro(request);

        assertNotNull(dto);
        assertEquals("Primera victoria", dto.getNombre());
        assertEquals("Hades II", dto.getTituloJuego());
        verify(logroRepository).save(any(Logro.class));
    }

    @Test
    @DisplayName("deleteLogro: elimina cuando el logro existe")
    void testDeleteLogro_exitoso() {
        when(logroRepository.existsById(1)).thenReturn(true);

        logroService.deleteLogro(1);

        verify(logroRepository).deleteById(1);
    }

    @Test
    @DisplayName("deleteLogro: lanza RecursoNoEncontradoException cuando no existe")
    void testDeleteLogro_noEncontrado() {
        when(logroRepository.existsById(999)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> logroService.deleteLogro(999));
        verify(logroRepository, never()).deleteById(any());
    }
}