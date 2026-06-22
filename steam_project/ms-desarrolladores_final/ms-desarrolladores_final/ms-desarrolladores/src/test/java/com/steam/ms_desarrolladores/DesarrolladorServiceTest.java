package com.steam.ms_desarrolladores;

import com.steam.ms_desarrolladores.client.JuegoClient;
import com.steam.ms_desarrolladores.client.dto.JuegoClientDTO;
import com.steam.ms_desarrolladores.exception.RecursoNoEncontradoException;
import com.steam.ms_desarrolladores.exception.ReglaDeNegocioException;
import com.steam.ms_desarrolladores.model.Desarrollador;
import com.steam.ms_desarrolladores.model.dto.DesarrolladorRequestDTO;
import com.steam.ms_desarrolladores.model.dto.DesarrolladorResponseDTO;
import com.steam.ms_desarrolladores.repository.DesarrolladorRepository;
import com.steam.ms_desarrolladores.service.DesarrolladorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesarrolladorServiceTest {

    @Mock private DesarrolladorRepository desarrolladorRepository;
    @Mock private JuegoClient juegoClient;

    @InjectMocks private DesarrolladorService desarrolladorService;

    private Desarrollador desarrollador;

    @BeforeEach
    void setUp() {
        desarrollador = new Desarrollador();
        desarrollador.setId(1);
        desarrollador.setNombre("Supergiant Games");
        desarrollador.setPais("Estados Unidos");
        desarrollador.setDescripcion("Estudio independiente");
        desarrollador.setSitioWeb("https://www.supergiantgames.com");
        desarrollador.setActivo(true);
    }

    private List<JuegoClientDTO> juegosDelDesarrollador() {
        JuegoClientDTO juego = new JuegoClientDTO();
        juego.setId(2);
        juego.setTitulo("Hades II");
        juego.setGenero("Roguelike");
        juego.setPrecio(new BigDecimal("19990"));
        juego.setDesarrollador("Supergiant Games");
        juego.setStock(10);
        juego.setActivo(true);
        return List.of(juego);
    }

    @Test
    @DisplayName("findById: retorna el desarrollador con sus juegos cuando existe")
    void testFindById_exitoso() {
        when(desarrolladorRepository.findById(1)).thenReturn(Optional.of(desarrollador));
        when(juegoClient.obtenerJuegosPorDesarrollador("Supergiant Games"))
                .thenReturn(juegosDelDesarrollador());

        DesarrolladorResponseDTO dto = desarrolladorService.findById(1);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Supergiant Games", dto.getNombre());
        assertEquals(1, dto.getJuegos().size());
        assertEquals("Hades II", dto.getJuegos().get(0).getTitulo());
    }

    @Test
    @DisplayName("findById: lanza RecursoNoEncontradoException cuando no existe")
    void testFindById_noEncontrado() {
        when(desarrolladorRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladorService.findById(999));
    }

    @Test
    @DisplayName("save: crea el desarrollador cuando el nombre no existe")
    void testSave_exitoso() {
        DesarrolladorRequestDTO request = new DesarrolladorRequestDTO();
        request.setNombre("Supergiant Games");
        request.setPais("Estados Unidos");
        request.setDescripcion("Estudio independiente");
        request.setSitioWeb("https://www.supergiantgames.com");

        when(desarrolladorRepository.existsByNombreIgnoreCase("Supergiant Games"))
                .thenReturn(false);
        when(desarrolladorRepository.save(any(Desarrollador.class)))
                .thenReturn(desarrollador);

        DesarrolladorResponseDTO dto = desarrolladorService.save(request);

        assertNotNull(dto);
        assertEquals("Supergiant Games", dto.getNombre());
        verify(desarrolladorRepository).save(any(Desarrollador.class));
    }

    @Test
    @DisplayName("save: lanza ReglaDeNegocioException con nombre duplicado")
    void testSave_nombreDuplicado() {
        DesarrolladorRequestDTO request = new DesarrolladorRequestDTO();
        request.setNombre("Supergiant Games");
        request.setPais("Estados Unidos");
        request.setDescripcion("Otra descripción");

        when(desarrolladorRepository.existsByNombreIgnoreCase("Supergiant Games"))
                .thenReturn(true);

        assertThrows(ReglaDeNegocioException.class,
                () -> desarrolladorService.save(request));
        verify(desarrolladorRepository, never()).save(any(Desarrollador.class));
    }

    @Test
    @DisplayName("delete: elimina cuando el desarrollador existe")
    void testDelete_exitoso() {
        when(desarrolladorRepository.existsById(1)).thenReturn(true);

        desarrolladorService.delete(1);

        verify(desarrolladorRepository).deleteById(1);
    }

    @Test
    @DisplayName("delete: lanza RecursoNoEncontradoException cuando no existe")
    void testDelete_noEncontrado() {
        when(desarrolladorRepository.existsById(999)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladorService.delete(999));
        verify(desarrolladorRepository, never()).deleteById(any());
    }
}
