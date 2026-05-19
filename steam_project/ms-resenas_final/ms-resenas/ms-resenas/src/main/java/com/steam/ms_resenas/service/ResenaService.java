package com.steam.ms_resenas.service;

import com.steam.ms_resenas.client.*;
import com.steam.ms_resenas.exception.*;
import com.steam.ms_resenas.model.ResenaModel;
import com.steam.ms_resenas.model.dto.*;
import com.steam.ms_resenas.repository.ResenaRepository;
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
public class ResenaService {

    private static final Logger log = LoggerFactory.getLogger(ResenaService.class);

    @Autowired private ResenaRepository  resenaRepository;
    @Autowired private UsuarioClient     usuarioClient;
    @Autowired private JuegoClient       juegoClient;
    @Autowired private BibliotecaClient  bibliotecaClient;

    public List<ResenaResponseDTO> findAll() {
        log.info("[ResenaService] Consultando todas las resenas");
        return resenaRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public ResenaResponseDTO findById(Integer id) {
        log.info("[ResenaService] Buscando resena id={}", id);
        ResenaModel resena = resenaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[ResenaService] Resena no encontrada id={}", id);
                    return new RecursoNoEncontradoException("Resena no encontrada con id: " + id);
                });
        return toResponseDTO(resena);
    }

    public ResenaResponseDTO save(ResenaRequestDTO dto) {
        log.info("[ResenaService] Creando resena usuarioId={} juegoId={}",
                dto.getUsuarioId(), dto.getJuegoId());

        UsuarioClientDTO usuario = validarUsuarioExiste(dto.getUsuarioId());
        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new ReglaDeNegocioException(
                    "El usuario con id " + dto.getUsuarioId() + " no esta activo");
        }

        JuegoClientDTO juego = validarJuegoExiste(dto.getJuegoId());
        if (Boolean.FALSE.equals(juego.getActivo())) {
            throw new ReglaDeNegocioException(
                    "El juego '" + juego.getTitulo() + "' no esta disponible");
        }

        validarUsuarioPoseeJuego(dto.getUsuarioId(), dto.getJuegoId());

        if (resenaRepository.existsByUsuarioIdAndJuegoId(dto.getUsuarioId(), dto.getJuegoId())) {
            log.warn("[ResenaService] Resena duplicada usuarioId={} juegoId={}",
                    dto.getUsuarioId(), dto.getJuegoId());
            throw new ReglaDeNegocioException(
                    "El usuario ya escribio una resena para el juego '" + juego.getTitulo() + "'");
        }

        ResenaModel resena = new ResenaModel();
        resena.setUsuarioId(dto.getUsuarioId());
        resena.setJuegoId(dto.getJuegoId());
        resena.setPuntuacion(dto.getPuntuacion());
        resena.setTitulo(dto.getTitulo());
        resena.setContenido(dto.getContenido());
        resena.setFecha(LocalDateTime.now());
        resena.setActivo(true);

        ResenaModel guardada = resenaRepository.save(resena);
        log.info("[ResenaService] Resena creada id={}", guardada.getId());
        return toResponseDTO(guardada);
    }

    public ResenaResponseDTO update(Integer id, ResenaRequestDTO dto) {
        log.info("[ResenaService] Actualizando resena id={}", id);
        ResenaModel resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Resena no encontrada con id: " + id));

        if (!resena.getUsuarioId().equals(dto.getUsuarioId())) {
            log.warn("[ResenaService] Usuario id={} intento editar resena de usuarioId={}",
                    dto.getUsuarioId(), resena.getUsuarioId());
            throw new ReglaDeNegocioException("Solo el autor puede editar esta resena");
        }

        resena.setPuntuacion(dto.getPuntuacion());
        resena.setTitulo(dto.getTitulo());
        resena.setContenido(dto.getContenido());

        ResenaModel actualizada = resenaRepository.save(resena);
        log.info("[ResenaService] Resena actualizada id={}", actualizada.getId());
        return toResponseDTO(actualizada);
    }

    public void delete(Integer id) {
        log.info("[ResenaService] Eliminando resena id={}", id);
        if (!resenaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Resena no encontrada con id: " + id);
        }
        resenaRepository.deleteById(id);
        log.info("[ResenaService] Resena eliminada id={}", id);
    }

    private UsuarioClientDTO validarUsuarioExiste(Integer usuarioId) {
        try {
            UsuarioClientDTO u = usuarioClient.obtenerUsuarioPorId(usuarioId);
            log.info("[ResenaService] Usuario id={} validado", usuarioId);
            return u;
        } catch (FeignException.NotFound e) {
            log.error("[ResenaService] Usuario id={} no encontrado", usuarioId);
            throw new RecursoNoEncontradoException("Usuario no encontrado con id: " + usuarioId);
        } catch (FeignException e) {
            log.error("[ResenaService] ms-usuarios no disponible: {}", e.getMessage());
            throw e;
        }
    }

    private JuegoClientDTO validarJuegoExiste(Integer juegoId) {
        try {
            JuegoClientDTO j = juegoClient.obtenerJuegoPorId(juegoId);
            log.info("[ResenaService] Juego id={} validado", juegoId);
            return j;
        } catch (FeignException.NotFound e) {
            log.error("[ResenaService] Juego id={} no encontrado", juegoId);
            throw new RecursoNoEncontradoException("Juego no encontrado con id: " + juegoId);
        } catch (FeignException e) {
            log.error("[ResenaService] ms-juegos no disponible: {}", e.getMessage());
            throw e;
        }
    }

    private void validarUsuarioPoseeJuego(Integer usuarioId, Integer juegoId) {
        try {
            List<BibliotecaClientDTO> biblioteca =
                    bibliotecaClient.obtenerBibliotecaDeUsuario(usuarioId);

            boolean poseeJuego = biblioteca.stream()
                    .anyMatch(e -> e.getJuegoId().equals(juegoId)
                               && Boolean.TRUE.equals(e.getActivo()));

            if (!poseeJuego) {
                log.warn("[ResenaService] Usuario id={} no posee el juego id={}",
                        usuarioId, juegoId);
                throw new ReglaDeNegocioException(
                        "El usuario debe poseer el juego para poder resenarlo");
            }
            log.info("[ResenaService] Posesion del juego id={} confirmada para usuario id={}",
                    juegoId, usuarioId);

        } catch (ReglaDeNegocioException e) {
            throw e;
        } catch (FeignException.NotFound e) {
            throw new ReglaDeNegocioException(
                    "El usuario no tiene juegos en su biblioteca");
        } catch (FeignException e) {
            log.error("[ResenaService] ms-biblioteca no disponible: {}", e.getMessage());
            throw e;
        }
    }

    private ResenaResponseDTO toResponseDTO(ResenaModel resena) {
        ResenaResponseDTO dto = new ResenaResponseDTO();
        dto.setId(resena.getId());
        dto.setUsuarioId(resena.getUsuarioId());
        dto.setJuegoId(resena.getJuegoId());
        dto.setPuntuacion(resena.getPuntuacion());
        dto.setTitulo(resena.getTitulo());
        dto.setContenido(resena.getContenido());
        dto.setFecha(resena.getFecha());
        dto.setActivo(resena.getActivo());

        try {
            UsuarioClientDTO u = usuarioClient.obtenerUsuarioPorId(resena.getUsuarioId());
            dto.setUsernameUsuario(u.getUsername());
            dto.setNombreUsuario(u.getNombre() + " " + u.getApellido());
        } catch (FeignException e) {
            log.warn("[ResenaService] No se pudo enriquecer usuario id={}", resena.getUsuarioId());
            dto.setUsernameUsuario("N/A");
            dto.setNombreUsuario("N/A");
        }

        try {
            JuegoClientDTO j = juegoClient.obtenerJuegoPorId(resena.getJuegoId());
            dto.setTituloJuego(j.getTitulo());
            dto.setGeneroJuego(j.getGenero());
            dto.setDesarrolladorJuego(j.getDesarrollador());
        } catch (FeignException e) {
            log.warn("[ResenaService] No se pudo enriquecer juego id={}", resena.getJuegoId());
            dto.setTituloJuego("N/A");
            dto.setGeneroJuego("N/A");
            dto.setDesarrolladorJuego("N/A");
        }

        return dto;
    }
}