package com.steam.ms_pagos;

import com.steam.ms_pagos.client.UsuarioClient;
import com.steam.ms_pagos.client.dto.UsuarioClientDTO;
import com.steam.ms_pagos.exception.RecursoNoEncontradoException;
import com.steam.ms_pagos.exception.ReglaDeNegocioException;
import com.steam.ms_pagos.model.Pago;
import com.steam.ms_pagos.model.dto.PagoRequestDTO;
import com.steam.ms_pagos.model.dto.PagoResponseDTO;
import com.steam.ms_pagos.repository.PagoRepository;
import com.steam.ms_pagos.service.PagoService;
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
class PagoServiceTest {

    @Mock private PagoRepository pagoRepository;
    @Mock private UsuarioClient usuarioClient;

    @InjectMocks private PagoService pagoService;

    private UsuarioClientDTO usuario;
    private Pago pago;
    private PagoRequestDTO request;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioClientDTO();
        usuario.setId(1);
        usuario.setUsername("gamer01");
        usuario.setNombre("Ana");
        usuario.setApellido("Pérez");
        usuario.setSaldo(new BigDecimal("50000"));
        usuario.setActivo(true);

        pago = new Pago();
        pago.setId(3);
        pago.setUsuarioId(1);
        pago.setMonto(new BigDecimal("10000"));
        pago.setMetodoPago("TARJETA");
        pago.setEstado("PENDIENTE");
        pago.setFechaPago(LocalDateTime.now());
        pago.setDescripcion("Recarga de saldo");

        request = new PagoRequestDTO();
        request.setUsuarioId(1);
        request.setMonto(new BigDecimal("10000"));
        request.setMetodoPago("TARJETA");
        request.setDescripcion("Recarga de saldo");
    }

    @Test
    @DisplayName("findById: retorna el pago enriquecido cuando existe")
    void testFindById_exitoso() {
        when(pagoRepository.findById(3)).thenReturn(Optional.of(pago));
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);

        PagoResponseDTO dto = pagoService.findById(3);

        assertNotNull(dto);
        assertEquals(3, dto.getId());
        assertEquals("gamer01", dto.getUsernameUsuario());
    }

    @Test
    @DisplayName("findById: lanza RecursoNoEncontradoException cuando no existe")
    void testFindById_noEncontrado() {
        when(pagoRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> pagoService.findById(999));
    }

    @Test
    @DisplayName("procesarPago: completa el pago y agrega saldo en el camino feliz")
    void testProcesarPago_exitoso() {
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        PagoResponseDTO dto = pagoService.procesarPago(request);

        assertNotNull(dto);
        assertEquals("COMPLETADO", dto.getEstado());
        verify(usuarioClient).agregarSaldo(1, new BigDecimal("10000"));
        verify(pagoRepository, times(2)).save(any(Pago.class));
    }

    @Test
    @DisplayName("procesarPago: rechaza usuario inactivo")
    void testProcesarPago_usuarioInactivo() {
        usuario.setActivo(false);
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);

        assertThrows(ReglaDeNegocioException.class,
                () -> pagoService.procesarPago(request));
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    @DisplayName("procesarPago: rechaza metodo de pago invalido")
    void testProcesarPago_metodoInvalido() {
        request.setMetodoPago("BITCOIN");
        when(usuarioClient.obtenerUsuarioPorId(1)).thenReturn(usuario);

        assertThrows(ReglaDeNegocioException.class,
                () -> pagoService.procesarPago(request));
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    @DisplayName("delete: elimina cuando el pago existe")
    void testDelete_exitoso() {
        when(pagoRepository.existsById(3)).thenReturn(true);

        pagoService.delete(3);

        verify(pagoRepository).deleteById(3);
    }

    @Test
    @DisplayName("delete: lanza RecursoNoEncontradoException cuando no existe")
    void testDelete_noEncontrado() {
        when(pagoRepository.existsById(999)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> pagoService.delete(999));
        verify(pagoRepository, never()).deleteById(any());
    }
}
