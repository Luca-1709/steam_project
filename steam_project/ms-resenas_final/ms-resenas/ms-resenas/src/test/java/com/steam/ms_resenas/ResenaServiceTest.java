package com.steam.ms_resenas;

import com.steam.ms_resenas.client.BibliotecaClient;
import com.steam.ms_resenas.client.JuegoClient;
import com.steam.ms_resenas.client.UsuarioClient;
import com.steam.ms_resenas.exception.RecursoNoEncontradoException;
import com.steam.ms_resenas.exception.ReglaDeNegocioException;
import com.steam.ms_resenas.model.ResenaModel;
import com.steam.ms_resenas.model.dto.BibliotecaClientDTO;
import com.steam.ms_resenas.model.dto.JuegoClientDTO;
import com.steam.ms_resenas.model.dto.ResenaRequestDTO;
import com.steam.ms_resenas.model.dto.ResenaResponseDTO;
import com.steam.ms_resenas.model.dto.UsuarioClientDTO;
import com.steam.ms_resenas.repository.ResenaRepository;
import com.steam.ms_resenas.service.ResenaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    @Mock private ResenaRepository resenaRepository;
    @Mock private UsuarioClient usuarioClient;
    @Mock private JuegoClient juegoClient;
    @Mock private BibliotecaClient bibliotecaClient;

    @InjectMocks private ResenaService resenaService;

    private UsuarioClientDTO usuario;
    private JuegoClientDTO juego;
    private ResenaModel resena;
    private ResenaRequestDTO request;

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
        juego.setGenero("Roguelike");
        juego.setDesarrollador("Supergiant");
        juego.setActivo(true);

        resena = new ResenaModel();
        resena.setId(5);
        resena.setUsuarioId(1);
        resena.setJuegoId(2);
        resena.setPuntuacion(5);
        resena.setTitulo("Una obra maestra");
        resena.setContenido("Excelente juego de principio a fin.");
        resena.setFecha(LocalDateTime.now());
        resena.setActivo(true);

        request = new ResenaRequestDTO();
        request.setUsuarioId(1);
        request.setJuegoId(2);
        request.setPuntuacion(5);
        request.setTitulo("Una obra maestra");
        request.setContenido("Excelente juego de principio a fin.");
    }

    private List<BibliotecaClientDTO> bibliotecaConJuego() {
        BibliotecaClientDTO entrada = new BibliotecaClientDTO();
        entrada.setUsuarioId(1);
        entrada.setJuegoId(2);
        entrada.setActivo(true);
        return List.of(entrada);
    }

    @Test
    @DisplayName("findById: retorna la resena enriquecida cuando existe")
    void testFindById_exitoso() {
        when(resenaRepository.findById(5)).thenReturn(Optional.of(resena));
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);
        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juego);

        ResenaResponseDTO dto = resenaService.findById(5);

        assertNotNull(dto);
        assertEquals(5, dto.getId());
        assertEquals("Hades II", dto.getTituloJuego());
        assertEquals("gamer01", dto.getUsernameUsuario());
    }

    @Test
    @DisplayName("findById: lanza RecursoNoEncontradoException cuando no existe")
    void testFindById_noEncontrado() {
        when(resenaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> resenaService.findById(999));
    }

    @Test
    @DisplayName("save: crea la resena en el camino feliz")
    void testSave_exitoso() {
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);
        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juego);
        when(bibliotecaClient.obtenerBibliotecaDeUsuario(1)).thenReturn(bibliotecaConJuego());
        when(resenaRepository.existsByUsuarioIdAndJuegoId(1, 2)).thenReturn(false);
        when(resenaRepository.save(any(ResenaModel.class))).thenReturn(resena);

        ResenaResponseDTO dto = resenaService.save(request);

        assertNotNull(dto);
        assertEquals("Hades II", dto.getTituloJuego());
        verify(resenaRepository).save(any(ResenaModel.class));
    }

    @Test
    @DisplayName("save: rechaza usuario inactivo")
    void testSave_usuarioInactivo() {
        usuario.setActivo(false);
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);

        assertThrows(ReglaDeNegocioException.class, () -> resenaService.save(request));
        verify(resenaRepository, never()).save(any(ResenaModel.class));
    }

    @Test
    @DisplayName("save: rechaza si el usuario no posee el juego")
    void testSave_noPoseeJuego() {
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);
        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juego);
        when(bibliotecaClient.obtenerBibliotecaDeUsuario(1)).thenReturn(List.of());

        assertThrows(ReglaDeNegocioException.class, () -> resenaService.save(request));
        verify(resenaRepository, never()).save(any(ResenaModel.class));
    }

    @Test
    @DisplayName("save: rechaza resena duplicada")
    void testSave_resenaDuplicada() {
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);
        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juego);
        when(bibliotecaClient.obtenerBibliotecaDeUsuario(1)).thenReturn(bibliotecaConJuego());
        when(resenaRepository.existsByUsuarioIdAndJuegoId(1, 2)).thenReturn(true);

        assertThrows(ReglaDeNegocioException.class, () -> resenaService.save(request));
        verify(resenaRepository, never()).save(any(ResenaModel.class));
    }
}