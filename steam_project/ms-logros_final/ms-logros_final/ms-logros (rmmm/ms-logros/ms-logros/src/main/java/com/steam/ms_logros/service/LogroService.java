package com.steam.ms_logros.service;

import com.steam.ms_logros.client.*;
import com.steam.ms_logros.model.dto.*;
import com.steam.ms_logros.exception.*;
import com.steam.ms_logros.model.*;
import com.steam.ms_logros.repository.*;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LogroService {

    private static final Logger log = LoggerFactory.getLogger(LogroService.class);

    @Autowired private LogroRepository        logroRepository;
    @Autowired private JuegoClient            juegoClient;

    public List<LogroResponseDTO> findAllLogros() {
        log.info("[LogroService] Consultando todos los logros");
        return logroRepository.findAll()
                .stream().map(this::toLogroResponseDTO).collect(Collectors.toList());
    }

    public LogroResponseDTO findLogroById(Integer id) {
        log.info("[LogroService] Buscando logro id={}", id);
        Logro logro = logroRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[LogroService] Logro no encontrado id={}", id);
                    return new RecursoNoEncontradoException("Logro no encontrado con id: " + id);
                });
        return toLogroResponseDTO(logro);
    }

    public LogroResponseDTO saveLogro(LogroRequestDTO dto) {
        log.info("[LogroService] Creando logro nombre={}", dto.getNombre());
        validarJuegoExiste(dto.getJuegoId());

        Logro logro = new Logro();
        logro.setNombre(dto.getNombre());
        logro.setDescripcion(dto.getDescripcion());
        logro.setJuegoId(dto.getJuegoId());
        logro.setCondicion(dto.getCondicion());

        Logro guardado = logroRepository.save(logro);
        log.info("[LogroService] Logro creado id={}", guardado.getId());
        return toLogroResponseDTO(guardado);
    }

    public void deleteLogro(Integer id) {
        log.info("[LogroService] Eliminando logro id={}", id);
        if (!logroRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Logro no encontrado con id: " + id);
        }
        logroRepository.deleteById(id);
        log.info("[LogroService] Logro eliminado id={}", id);
    }


    private JuegoClientDTO validarJuegoExiste(Integer juegoId) {
        try {
            JuegoClientDTO j = juegoClient.obtenerJuegoPorId(juegoId);
            log.info("[LogroService] Juego id={} validado", juegoId);
            return j;
        } catch (FeignException.NotFound e) {
            log.error("[LogroService] Juego id={} no encontrado", juegoId);
            throw new RecursoNoEncontradoException("Juego no encontrado con id: " + juegoId);
        } catch (FeignException e) {
            log.error("[LogroService] ms-juegos no disponible: {}", e.getMessage());
            throw e;
        }
    }

    private LogroResponseDTO toLogroResponseDTO(Logro logro) {
        LogroResponseDTO dto = new LogroResponseDTO();
        dto.setId(logro.getId());
        dto.setNombre(logro.getNombre());
        dto.setDescripcion(logro.getDescripcion());
        dto.setJuegoId(logro.getJuegoId());
        dto.setCondicion(logro.getCondicion());

        try {
            JuegoClientDTO juego = juegoClient.obtenerJuegoPorId(logro.getJuegoId());
            dto.setTituloJuego(juego.getTitulo());
            dto.setGeneroJuego(juego.getGenero());
        } catch (FeignException e) {
            log.warn("[LogroService] No se pudo enriquecer juego id={}", logro.getJuegoId());
            dto.setTituloJuego("N/A");
            dto.setGeneroJuego("N/A");
        }
        return dto;
    }

}