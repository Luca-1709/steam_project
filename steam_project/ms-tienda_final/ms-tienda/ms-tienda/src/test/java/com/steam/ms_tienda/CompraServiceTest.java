package com.steam.ms_tienda;

import com.steam.ms_tienda.client.BibliotecaClient;
import com.steam.ms_tienda.client.JuegoClient;
import com.steam.ms_tienda.client.UsuarioClient;
import com.steam.ms_tienda.exception.RecursoNoEncontradoException;
import com.steam.ms_tienda.exception.ReglaDeNegocioException;
import com.steam.ms_tienda.model.Compra;
import com.steam.ms_tienda.model.dto.CompraRequestDTO;
import com.steam.ms_tienda.model.dto.CompraResponseDTO;
import com.steam.ms_tienda.model.dto.JuegoClientDTO;
import com.steam.ms_tienda.model.dto.UsuarioClientDTO;
import com.steam.ms_tienda.repository.CompraRepository;
import com.steam.ms_tienda.service.CompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {

    @Mock private CompraRepository compraRepository;
    @Mock private UsuarioClient usuarioClient;
    @Mock private JuegoClient juegoClient;
    @Mock private BibliotecaClient bibliotecaClient;

    @InjectMocks private CompraService compraService;

    private UsuarioClientDTO usuarioActivo;
    private JuegoClientDTO juegoDisponible;
    private CompraRequestDTO request;

    @BeforeEach
    void setUp() {
        usuarioActivo = new UsuarioClientDTO();
        usuarioActivo.setId(1);
        usuarioActivo.setUsername("gamer01");
        usuarioActivo.setNombre("Ana");
        usuarioActivo.setApellido("Pérez");
        usuarioActivo.setSaldo(new BigDecimal("50000"));
        usuarioActivo.setActivo(true);

        juegoDisponible = new JuegoClientDTO();
        juegoDisponible.setId(2);
        juegoDisponible.setTitulo("Hades II");
        juegoDisponible.setPrecio(new BigDecimal("19990"));
        juegoDisponible.setGenero("Roguelike");
        juegoDisponible.setDesarrollador("Supergiant");
        juegoDisponible.setStock(10);
        juegoDisponible.setActivo(true);

        request = new CompraRequestDTO();
        request.setUsuarioId(1);
        request.setJuegoId(2);
        request.setMetodoPago("SALDO");
    }

    private Compra compraGuardada() {
        Compra c = new Compra();
        c.setId(100);
        c.setUsuarioId(1);
        c.setJuegoId(2);
        c.setPrecioPagado(new BigDecimal("19990"));
        c.setFechaCompra(LocalDateTime.now());
        c.setEstado("COMPLETADA");
        c.setMetodoPago("SALDO");
        return c;
    }

    @Test
    @DisplayName("findById: retorna la compra enriquecida cuando existe")
    void testFindById_exitoso() {
        when(compraRepository.findById(100)).thenReturn(Optional.of(compraGuardada()));
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuarioActivo);
        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juegoDisponible);

        CompraResponseDTO dto = compraService.findById(100);

        assertNotNull(dto);
        assertEquals(100, dto.getId());
        assertEquals("Hades II", dto.getTituloJuego());
        assertEquals("gamer01", dto.getUsernameUsuario());
    }

    @Test
    @DisplayName("findById: lanza RecursoNoEncontradoException cuando no existe")
    void testFindById_noEncontrado() {
        when(compraRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> compraService.findById(999));
    }

    @Test
    @DisplayName("realizarCompra: registra la compra en el camino feliz")
    void testRealizarCompra_exitosa() {
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuarioActivo);
        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juegoDisponible);
        when(compraRepository.existsByUsuarioIdAndJuegoId(1, 2)).thenReturn(false);
        when(compraRepository.save(any(Compra.class))).thenReturn(compraGuardada());

        CompraResponseDTO dto = compraService.realizarCompra(request);

        assertNotNull(dto);
        assertEquals("COMPLETADA", dto.getEstado());
        assertEquals(0, dto.getPrecioPagado().compareTo(new BigDecimal("19990")));
        verify(compraRepository).save(any(Compra.class));
        verify(juegoClient).descontarStock(2, 1);
        verify(bibliotecaClient).agregarABiblioteca(any());
    }

    @Test
    @DisplayName("realizarCompra: rechaza usuario inactivo")
    void testRealizarCompra_usuarioInactivo() {
        usuarioActivo.setActivo(false);
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuarioActivo);

        assertThrows(ReglaDeNegocioException.class,
                () -> compraService.realizarCompra(request));
        verify(compraRepository, never()).save(any(Compra.class));
    }

    @Test
    @DisplayName("realizarCompra: rechaza saldo insuficiente")
    void testRealizarCompra_saldoInsuficiente() {
        usuarioActivo.setSaldo(new BigDecimal("5000"));
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuarioActivo);
        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juegoDisponible);
        when(compraRepository.existsByUsuarioIdAndJuegoId(1, 2)).thenReturn(false);

        assertThrows(ReglaDeNegocioException.class,
                () -> compraService.realizarCompra(request));
        verify(compraRepository, never()).save(any(Compra.class));
    }

    @Test
    @DisplayName("realizarCompra: rechaza juego sin stock")
    void testRealizarCompra_juegoSinStock() {
        juegoDisponible.setStock(0);
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuarioActivo);
        when(juegoClient.obtenerJuegoPorId(2)).thenReturn(juegoDisponible);

        assertThrows(ReglaDeNegocioException.class,
                () -> compraService.realizarCompra(request));
        verify(compraRepository, never()).save(any(Compra.class));
    }
}