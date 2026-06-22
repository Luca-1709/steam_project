package com.steam.ms_usuarios;

import com.steam.ms_usuarios.Model.UsuarioModel;
import com.steam.ms_usuarios.Model.dto.UsuarioResponseDTO;
import com.steam.ms_usuarios.Repository.UsuarioRepository;
import com.steam.ms_usuarios.Service.UsuarioService;
import com.steam.ms_usuarios.exception.RecursoNoEncontradoException;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Faker faker;
    private UsuarioModel usuarioMock;

    @BeforeEach
    void setUp() {
        faker = new Faker();

        usuarioMock = new UsuarioModel();
        usuarioMock.setId(1);
        usuarioMock.setNombre(faker.name().firstName());
        usuarioMock.setApellido(faker.name().lastName());
        usuarioMock.setUsername(faker.name().username());
        usuarioMock.setEmail(faker.internet().emailAddress());
        usuarioMock.setPassword(faker.internet().password());
        usuarioMock.setPais(faker.address().country());
        usuarioMock.setSaldo(BigDecimal.valueOf(50000));
        usuarioMock.setFechaRegistro(LocalDate.now());
        usuarioMock.setActivo(true);
    }

    @Test
    @DisplayName("Debe retornar usuario cuando existe el ID")
    void testFindById_exitoso() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioMock));

        UsuarioResponseDTO resultado = usuarioService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(usuarioMock.getNombre(), resultado.getNombre());
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando el usuario no existe")
    void testFindById_noEncontrado() {
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> usuarioService.findById(999));
    }
}
