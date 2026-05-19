package com.steam.ms_desarrolladores.service;

import com.steam.ms_desarrolladores.client.JuegoClient;
import com.steam.ms_desarrolladores.client.dto.JuegoClientDTO;
import com.steam.ms_desarrolladores.model.dto.*;
import com.steam.ms_desarrolladores.exception.*;
import com.steam.ms_desarrolladores.model.Desarrollador;
import com.steam.ms_desarrolladores.repository.DesarrolladorRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DesarrolladorService {

    private static final Logger log = LoggerFactory.getLogger(DesarrolladorService.class);

    @Autowired private DesarrolladorRepository desarrolladorRepository;
    @Autowired private JuegoClient             juegoClient;

    public List<DesarrolladorResponseDTO> findAll() {
        log.info("[DesarrolladorService] Consultando todos los desarrolladores");
        return desarrolladorRepository.findAll()
                .stream().map(d -> toResponseDTO(d, false))
                .collect(Collectors.toList());
    }

    public DesarrolladorResponseDTO findById(Integer id) {
        log.info("[DesarrolladorService] Buscando desarrollador id={}", id);
        Desarrollador desarrollador = desarrolladorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[DesarrolladorService] Desarrollador no encontrado id={}", id);
                    return new RecursoNoEncontradoException(
                            "Desarrollador no encontrado con id: " + id);
                });
        return toResponseDTO(desarrollador, true);
    }

    public DesarrolladorResponseDTO save(DesarrolladorRequestDTO dto) {
        log.info("[DesarrolladorService] Creando desarrollador nombre={}", dto.getNombre());

        if (desarrolladorRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            log.warn("[DesarrolladorService] Nombre duplicado: {}", dto.getNombre());
            throw new ReglaDeNegocioException(
                    "Ya existe un desarrollador con el nombre: " + dto.getNombre());
        }

        Desarrollador desarrollador = new Desarrollador();
        desarrollador.setNombre(dto.getNombre());
        desarrollador.setPais(dto.getPais());
        desarrollador.setDescripcion(dto.getDescripcion());
        desarrollador.setSitioWeb(dto.getSitioWeb());
        desarrollador.setActivo(true);

        Desarrollador guardado = desarrolladorRepository.save(desarrollador);
        log.info("[DesarrolladorService] Desarrollador creado id={}", guardado.getId());
        return toResponseDTO(guardado, false);
    }

    public DesarrolladorResponseDTO update(Integer id, DesarrolladorRequestDTO dto) {
        log.info("[DesarrolladorService] Actualizando desarrollador id={}", id);
        Desarrollador desarrollador = desarrolladorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Desarrollador no encontrado con id: " + id));

        desarrollador.setNombre(dto.getNombre());
        desarrollador.setPais(dto.getPais());
        desarrollador.setDescripcion(dto.getDescripcion());
        desarrollador.setSitioWeb(dto.getSitioWeb());

        Desarrollador actualizado = desarrolladorRepository.save(desarrollador);
        log.info("[DesarrolladorService] Desarrollador actualizado id={}", actualizado.getId());
        return toResponseDTO(actualizado, false);
    }

    public void delete(Integer id) {
        log.info("[DesarrolladorService] Eliminando desarrollador id={}", id);
        if (!desarrolladorRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Desarrollador no encontrado con id: " + id);
        }
        desarrolladorRepository.deleteById(id);
        log.info("[DesarrolladorService] Desarrollador eliminado id={}", id);
    }

    private DesarrolladorResponseDTO toResponseDTO(Desarrollador d, boolean conJuegos) {
        DesarrolladorResponseDTO dto = new DesarrolladorResponseDTO();
        dto.setId(d.getId());
        dto.setNombre(d.getNombre());
        dto.setPais(d.getPais());
        dto.setDescripcion(d.getDescripcion());
        dto.setSitioWeb(d.getSitioWeb());
        dto.setActivo(d.getActivo());

        if (conJuegos) {
            try {
                List<JuegoClientDTO> juegos =
                        juegoClient.obtenerJuegosPorDesarrollador(d.getNombre());
                dto.setJuegos(juegos.stream().map(j -> {
                    JuegoClientResponseDTO jDto = new JuegoClientResponseDTO();
                    jDto.setId(j.getId());
                    jDto.setTitulo(j.getTitulo());
                    jDto.setGenero(j.getGenero());
                    jDto.setPrecio(j.getPrecio());
                    jDto.setStock(j.getStock());
                    jDto.setActivo(j.getActivo());
                    return jDto;
                }).collect(Collectors.toList()));
                log.info("[DesarrolladorService] Juegos enriquecidos para desarrollador id={}",
                        d.getId());
            } catch (FeignException e) {
                log.warn("[DesarrolladorService] No se pudo obtener juegos para desarrollador id={}",
                        d.getId());
                dto.setJuegos(Collections.emptyList());
            }
        }

        return dto;
    }
}