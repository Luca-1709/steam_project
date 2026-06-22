package com.steam.ms_amigos;

import com.steam.ms_amigos.client.UsuarioClient;
import com.steam.ms_amigos.client.dto.UsuarioClientDTO;
import com.steam.ms_amigos.exception.RecursoNoEncontradoException;
import com.steam.ms_amigos.exception.ReglaDeNegocioException;
import com.steam.ms_amigos.model.Amistad;
import com.steam.ms_amigos.model.dto.AmistadRequestDTO;
import com.steam.ms_amigos.model.dto.AmistadResponseDTO;
import com.steam.ms_amigos.repository.AmistadRepository;
import com.steam.ms_amigos.service.AmistadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmistadServiceTest {

    @Mock private AmistadRepository amistadRepository;
    @Mock private UsuarioClient usuarioClient;

    @InjectMocks private AmistadService amistadService;

    private UsuarioClientDTO usuario;
    private UsuarioClientDTO amigo;
    private Amistad amistad;
    private AmistadRequestDTO request;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioClientDTO();
        usuario.setId(1);
        usuario.setUsername("gamer01");
        usuario.setNombre("Ana");
        usuario.setApellido("Pérez");
        usuario.setActivo(true);

        amigo = new UsuarioClientDTO();
        amigo.setId(2);
        amigo.setUsername("gamer02");
        amigo.setNombre("Beto");
        amigo.setApellido("Soto");
        amigo.setActivo(true);

        amistad = new Amistad();
        amistad.setId(7);
        amistad.setUsuarioId(1);
        amistad.setAmigoId(2);
        amistad.setFechaAmistad(LocalDateTime.now());
        amistad.setEstado("ACEPTADA");

        request = new AmistadRequestDTO();
        request.setUsuarioId(1);
        request.setAmigoId(2);
    }

    @Test
    @DisplayName("findById: retorna la amistad enriquecida cuando existe")
    void testFindById_exitoso() {
        when(amistadRepository.findById(7)).thenReturn(Optional.of(amistad));
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);
        when(usuarioClient.obtenerUsuarioPorId(2)).thenReturn(amigo);

        AmistadResponseDTO dto = amistadService.findById(7);

        assertNotNull(dto);
        assertEquals(7, dto.getId());
        assertEquals("gamer01", dto.getUsernameUsuario());
        assertEquals("gamer02", dto.getUsernameAmigo());
    }

    @Test
    @DisplayName("findById: lanza RecursoNoEncontradoException cuando no existe")
    void testFindById_noEncontrado() {
        when(amistadRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> amistadService.findById(999));
    }

    @Test
    @DisplayName("enviarSolicitud: crea la solicitud en el camino feliz")
    void testEnviarSolicitud_exitoso() {
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);
        when(usuarioClient.obtenerUsuarioPorId(2)).thenReturn(amigo);
        when(amistadRepository.existsByUsuarioIdAndAmigoId(1, 2)).thenReturn(false);
        when(amistadRepository.save(any(Amistad.class))).thenReturn(amistad);

        AmistadResponseDTO dto = amistadService.enviarSolicitud(request);

        assertNotNull(dto);
        assertEquals("gamer02", dto.getUsernameAmigo());
        verify(amistadRepository).save(any(Amistad.class));
    }

    @Test
    @DisplayName("enviarSolicitud: rechaza solicitud a si mismo")
    void testEnviarSolicitud_aSiMismo() {
        request.setAmigoId(1);

        assertThrows(ReglaDeNegocioException.class,
                () -> amistadService.enviarSolicitud(request));
        verify(amistadRepository, never()).save(any(Amistad.class));
    }

    @Test
    @DisplayName("enviarSolicitud: rechaza usuario inactivo")
    void testEnviarSolicitud_usuarioInactivo() {
        usuario.setActivo(false);
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);
        when(usuarioClient.obtenerUsuarioPorId(2)).thenReturn(amigo);

        assertThrows(ReglaDeNegocioException.class,
                () -> amistadService.enviarSolicitud(request));
        verify(amistadRepository, never()).save(any(Amistad.class));
    }

    @Test
    @DisplayName("enviarSolicitud: rechaza solicitud duplicada")
    void testEnviarSolicitud_duplicada() {
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);
        when(usuarioClient.obtenerUsuarioPorId(2)).thenReturn(amigo);
        when(amistadRepository.existsByUsuarioIdAndAmigoId(1, 2)).thenReturn(true);

        assertThrows(ReglaDeNegocioException.class,
                () -> amistadService.enviarSolicitud(request));
        verify(amistadRepository, never()).save(any(Amistad.class));
    }
}