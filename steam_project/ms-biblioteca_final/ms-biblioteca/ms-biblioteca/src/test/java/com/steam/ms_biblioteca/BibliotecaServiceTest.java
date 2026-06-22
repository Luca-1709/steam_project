package com.steam.ms_biblioteca;

import com.steam.ms_biblioteca.client.JuegoClient;
import com.steam.ms_biblioteca.client.UsuarioClient;
import com.steam.ms_biblioteca.exception.RecursoNoEncontradoException;
import com.steam.ms_biblioteca.model.BibliotecaModel;
import com.steam.ms_biblioteca.model.dto.BibliotecaRequestDTO;
import com.steam.ms_biblioteca.model.dto.BibliotecaResponseDTO;
import com.steam.ms_biblioteca.model.dto.JuegoClientDTO;
import com.steam.ms_biblioteca.model.dto.UsuarioClientDTO;
import com.steam.ms_biblioteca.repository.BibliotecaRepository;
import com.steam.ms_biblioteca.service.BibliotecaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BibliotecaServiceTest {

    @Mock private BibliotecaRepository bibliotecaRepository;
    @Mock private UsuarioClient usuarioClient;
    @Mock private JuegoClient juegoClient;

    @InjectMocks private BibliotecaService bibliotecaService;

    private UsuarioClientDTO usuario;
    private JuegoClientDTO juego;
    private BibliotecaModel entrada;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioClientDTO();
        usuario.setId(1);
        usuario.setUsername("gamer01");
        usuario.setNombre("Ana");
        usuario.setApellido("Pérez");
        usuario.setActivo(true);

        juego = new JuegoClientDTO();
        juego.setId(2);
        juego.setTitulo("Hades II");
        juego.setPrecio(new BigDecimal("19990"));
        juego.setGenero("Roguelike");
        juego.setDesarrollador("Supergiant");
        juego.setActivo(true);

        entrada = new BibliotecaModel();
        entrada.setId(10);
        entrada.setUsuarioId(1);
        entrada.setJuegoId(2);
        entrada.setFechaAdquisicion(LocalDate.now());
        entrada.setHorasJugadas(42);
        entrada.setActivo(true);
    }

    @Test
    @DisplayName("findById: retorna la entrada enriquecida cuando existe")
    void testFindById_exitoso() {
        when(bibliotecaRepository.findById(10)).thenReturn(Optional.of(entrada));
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);
        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juego);

        BibliotecaResponseDTO dto = bibliotecaService.findById(10);

        assertNotNull(dto);
        assertEquals(10, dto.getId());
        assertEquals("Hades II", dto.getTituloJuego());
        assertEquals("gamer01", dto.getUsernameUsuario());
    }

    @Test
    @DisplayName("findById: lanza RecursoNoEncontradoException cuando no existe")
    void testFindById_noEncontrado() {
        when(bibliotecaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> bibliotecaService.findById(999));
    }

    @Test
    @DisplayName("findByUsuarioId: lista las entradas del usuario")
    void testFindByUsuarioId_exitoso() {
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);
        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juego);
        when(bibliotecaRepository.findByUsuarioId(1)).thenReturn(List.of(entrada));

        List<BibliotecaResponseDTO> lista = bibliotecaService.findByUsuarioId(1);

        assertEquals(1, lista.size());
        assertEquals("Hades II", lista.get(0).getTituloJuego());
    }

    @Test
    @DisplayName("save: agrega el juego a la biblioteca")
    void testSave_exitoso() {
        BibliotecaRequestDTO request = new BibliotecaRequestDTO();
        request.setUsuarioId(1);
        request.setJuegoId(2);
        request.setHorasJugadas(0);

        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);
        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juego);
        when(bibliotecaRepository.save(any(BibliotecaModel.class))).thenReturn(entrada);

        BibliotecaResponseDTO dto = bibliotecaService.save(request);

        assertNotNull(dto);
        assertEquals(10, dto.getId());
        assertEquals("Hades II", dto.getTituloJuego());
        verify(bibliotecaRepository).save(any(BibliotecaModel.class));
    }

    @Test
    @DisplayName("delete: elimina cuando la entrada existe")
    void testDelete_exitoso() {
        when(bibliotecaRepository.existsById(10)).thenReturn(true);

        bibliotecaService.delete(10);

        verify(bibliotecaRepository).deleteById(10);
    }

    @Test
    @DisplayName("delete: lanza RecursoNoEncontradoException cuando no existe")
    void testDelete_noEncontrado() {
        when(bibliotecaRepository.existsById(999)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> bibliotecaService.delete(999));
        verify(bibliotecaRepository, never()).deleteById(any());
    }
}
