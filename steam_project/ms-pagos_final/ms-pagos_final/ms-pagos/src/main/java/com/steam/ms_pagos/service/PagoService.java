package com.steam.ms_pagos.service;

import com.steam.ms_pagos.client.UsuarioClient;
import com.steam.ms_pagos.client.dto.UsuarioClientDTO;
import com.steam.ms_pagos.model.dto.*;
import com.steam.ms_pagos.exception.*;
import com.steam.ms_pagos.model.Pago;
import com.steam.ms_pagos.repository.PagoRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    @Autowired private PagoRepository pagoRepository;
    @Autowired private UsuarioClient  usuarioClient;

    private static final List<String> METODOS_VALIDOS =
            List.of("TARJETA", "TRANSFERENCIA");

    public List<PagoResponseDTO> findAll() {
        log.info("[PagoService] Consultando todos los pagos");
        return pagoRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public PagoResponseDTO findById(Integer id) {
        log.info("[PagoService] Buscando pago id={}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[PagoService] Pago no encontrado id={}", id);
                    return new RecursoNoEncontradoException(
                            "Pago no encontrado con id: " + id);
                });
        return toResponseDTO(pago);
    }

    public PagoResponseDTO procesarPago(PagoRequestDTO dto) {
        log.info("[PagoService] Procesando pago usuarioId={} monto={}",
                dto.getUsuarioId(), dto.getMonto());

        UsuarioClientDTO usuario = validarUsuarioExiste(dto.getUsuarioId());
        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new ReglaDeNegocioException(
                    "El usuario con id " + dto.getUsuarioId() + " no esta activo");
        }

        if (!METODOS_VALIDOS.contains(dto.getMetodoPago().toUpperCase())) {
            log.warn("[PagoService] Metodo de pago invalido: {}", dto.getMetodoPago());
            throw new ReglaDeNegocioException(
                    "Metodo de pago invalido. Use: TARJETA O TRANSFERENCIA");
        }

        Pago pago = new Pago();
        pago.setUsuarioId(dto.getUsuarioId());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago().toUpperCase());
        pago.setEstado("PENDIENTE");
        pago.setFechaPago(LocalDateTime.now());
        pago.setDescripcion(dto.getDescripcion());

        Pago guardado = pagoRepository.save(pago);
        log.info("[PagoService] Pago registrado id={} en estado PENDIENTE", guardado.getId());

        try {
            usuarioClient.agregarSaldo(dto.getUsuarioId(), dto.getMonto());
            guardado.setEstado("COMPLETADO");
            pagoRepository.save(guardado);
            log.info("[PagoService] Saldo agregado y pago id={} COMPLETADO", guardado.getId());
        } catch (FeignException e) {
            guardado.setEstado("RECHAZADO");
            pagoRepository.save(guardado);
            log.error("[PagoService] Error al agregar saldo, pago id={} RECHAZADO: {}",
                    guardado.getId(), e.getMessage());
            throw new ReglaDeNegocioException(
                    "No se pudo procesar el pago. Intente mas tarde.");
        }

        return toResponseDTO(guardado);
    }

    public PagoResponseDTO update(Integer id, PagoRequestDTO dto) {
        log.info("[PagoService] Actualizando pago id={}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Pago no encontrado con id: " + id));

        pago.setDescripcion(dto.getDescripcion());
        pago.setMetodoPago(dto.getMetodoPago().toUpperCase());

        Pago actualizado = pagoRepository.save(pago);
        log.info("[PagoService] Pago actualizado id={}", actualizado.getId());
        return toResponseDTO(actualizado);
    }

    public void delete(Integer id) {
        log.info("[PagoService] Eliminando pago id={}", id);
        if (!pagoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Pago no encontrado con id: " + id);
        }
        pagoRepository.deleteById(id);
        log.info("[PagoService] Pago eliminado id={}", id);
    }

    private UsuarioClientDTO validarUsuarioExiste(Integer usuarioId) {
        try {
            UsuarioClientDTO u = usuarioClient.obtenerUsuarioPorId(usuarioId);
            log.info("[PagoService] Usuario id={} validado", usuarioId);
            return u;
        } catch (FeignException.NotFound e) {
            log.error("[PagoService] Usuario id={} no encontrado", usuarioId);
            throw new RecursoNoEncontradoException(
                    "Usuario no encontrado con id: " + usuarioId);
        } catch (FeignException e) {
            log.error("[PagoService] ms-usuarios no disponible: {}", e.getMessage());
            throw e;
        }
    }

    private PagoResponseDTO toResponseDTO(Pago pago) {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setId(pago.getId());
        dto.setUsuarioId(pago.getUsuarioId());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstado(pago.getEstado());
        dto.setFechaPago(pago.getFechaPago());
        dto.setDescripcion(pago.getDescripcion());

        try {
            UsuarioClientDTO usuario =
                    usuarioClient.obtenerUsuarioPorId(pago.getUsuarioId());
            dto.setUsernameUsuario(usuario.getUsername());
            dto.setNombreUsuario(usuario.getNombre() + " " + usuario.getApellido());
        } catch (FeignException e) {
            log.warn("[PagoService] No se pudo enriquecer usuario id={}", pago.getUsuarioId());
            dto.setUsernameUsuario("N/A");
            dto.setNombreUsuario("N/A");
        }

        return dto;
    }
}