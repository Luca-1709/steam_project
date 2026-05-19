package com.steam.ms_amigos.service;

import com.steam.ms_amigos.client.UsuarioClient;
import com.steam.ms_amigos.client.dto.UsuarioClientDTO;
import com.steam.ms_amigos.exception.*;
import com.steam.ms_amigos.model.Amistad;
import com.steam.ms_amigos.model.dto.*;
import com.steam.ms_amigos.repository.AmistadRepository;
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
public class AmistadService {

    private static final Logger log = LoggerFactory.getLogger(AmistadService.class);

    @Autowired private AmistadRepository amistadRepository;
    @Autowired private UsuarioClient     usuarioClient;

    public List<AmistadResponseDTO> findAmigosDeUsuario(Integer usuarioId) {
        log.info("[AmistadService] Consultando amigos del usuario id={}", usuarioId);
        validarUsuarioExiste(usuarioId);
        return amistadRepository.findByUsuarioIdAndEstado(usuarioId, "ACEPTADA")
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public AmistadResponseDTO findById(Integer id) {
        log.info("[AmistadService] Buscando amistad id={}", id);
        Amistad amistad = amistadRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[AmistadService] Amistad no encontrada id={}", id);
                    return new RecursoNoEncontradoException(
                            "Amistad no encontrada con id: " + id);
                });
        return toResponseDTO(amistad);
    }

    public AmistadResponseDTO enviarSolicitud(AmistadRequestDTO dto) {
        log.info("[AmistadService] Enviando solicitud usuarioId={} amigoId={}",
                dto.getUsuarioId(), dto.getAmigoId());

        if (dto.getUsuarioId().equals(dto.getAmigoId())) {
            throw new ReglaDeNegocioException(
                    "Un usuario no puede enviarse una solicitud de amistad a si mismo");
        }

        UsuarioClientDTO usuario = validarUsuarioExiste(dto.getUsuarioId());
        UsuarioClientDTO amigo   = validarUsuarioExiste(dto.getAmigoId());

        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new ReglaDeNegocioException(
                    "El usuario con id " + dto.getUsuarioId() + " no esta activo");
        }
        if (Boolean.FALSE.equals(amigo.getActivo())) {
            throw new ReglaDeNegocioException(
                    "El usuario con id " + dto.getAmigoId() + " no esta activo");
        }

        if (amistadRepository.existsByUsuarioIdAndAmigoId(
                dto.getUsuarioId(), dto.getAmigoId())) {
            log.warn("[AmistadService] Solicitud duplicada usuarioId={} amigoId={}",
                    dto.getUsuarioId(), dto.getAmigoId());
            throw new ReglaDeNegocioException(
                    "Ya existe una solicitud entre estos usuarios");
        }

        Amistad amistad = new Amistad();
        amistad.setUsuarioId(dto.getUsuarioId());
        amistad.setAmigoId(dto.getAmigoId());
        amistad.setFechaAmistad(LocalDateTime.now());
        amistad.setEstado("PENDIENTE");

        Amistad guardada = amistadRepository.save(amistad);
        log.info("[AmistadService] Solicitud creada id={}", guardada.getId());
        return toResponseDTO(guardada);
    }

    public void delete(Integer id) {
        log.info("[AmistadService] Eliminando amistad id={}", id);
        if (!amistadRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Amistad no encontrada con id: " + id);
        }
        amistadRepository.deleteById(id);
        log.info("[AmistadService] Amistad eliminada id={}", id);
    }

    private UsuarioClientDTO validarUsuarioExiste(Integer usuarioId) {
        try {
            UsuarioClientDTO u = usuarioClient.obtenerUsuarioPorId(usuarioId);
            log.info("[AmistadService] Usuario id={} validado", usuarioId);
            return u;
        } catch (FeignException.NotFound e) {
            log.error("[AmistadService] Usuario id={} no encontrado", usuarioId);
            throw new RecursoNoEncontradoException(
                    "Usuario no encontrado con id: " + usuarioId);
        } catch (FeignException e) {
            log.error("[AmistadService] ms-usuarios no disponible: {}", e.getMessage());
            throw e;
        }
    }

    private AmistadResponseDTO toResponseDTO(Amistad amistad) {
        AmistadResponseDTO dto = new AmistadResponseDTO();
        dto.setId(amistad.getId());
        dto.setUsuarioId(amistad.getUsuarioId());
        dto.setAmigoId(amistad.getAmigoId());
        dto.setFechaAmistad(amistad.getFechaAmistad());
        dto.setEstado(amistad.getEstado());

        try {
            UsuarioClientDTO usuario = usuarioClient.obtenerUsuarioPorId(amistad.getUsuarioId());
            dto.setUsernameUsuario(usuario.getUsername());
            dto.setNombreUsuario(usuario.getNombre() + " " + usuario.getApellido());
        } catch (FeignException e) {
            log.warn("[AmistadService] No se pudo enriquecer usuario id={}", amistad.getUsuarioId());
            dto.setUsernameUsuario("N/A");
            dto.setNombreUsuario("N/A");
        }

        try {
            UsuarioClientDTO amigo = usuarioClient.obtenerUsuarioPorId(amistad.getAmigoId());
            dto.setUsernameAmigo(amigo.getUsername());
            dto.setNombreAmigo(amigo.getNombre() + " " + amigo.getApellido());
        } catch (FeignException e) {
            log.warn("[AmistadService] No se pudo enriquecer amigo id={}", amistad.getAmigoId());
            dto.setUsernameAmigo("N/A");
            dto.setNombreAmigo("N/A");
        }

        return dto;
    }
}