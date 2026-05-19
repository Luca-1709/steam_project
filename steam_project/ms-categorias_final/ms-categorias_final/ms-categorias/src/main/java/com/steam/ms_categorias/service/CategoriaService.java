package com.steam.ms_categorias.service;


import com.steam.ms_categorias.client.JuegoClient;
import com.steam.ms_categorias.client.dto.JuegoClientDTO;
import com.steam.ms_categorias.model.dto.*;
import com.steam.ms_categorias.exception.*;
import com.steam.ms_categorias.model.Categoria;
import com.steam.ms_categorias.repository.CategoriaRepository;
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
public class CategoriaService {

    private static final Logger log = LoggerFactory.getLogger(CategoriaService.class);

    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private JuegoClient         juegoClient;

    public List<CategoriaResponseDTO> findAll() {
        log.info("[CategoriaService] Consultando todas las categorias");
        return categoriaRepository.findAll()
                .stream().map(c -> toResponseDTO(c, false))
                .collect(Collectors.toList());
    }

    public CategoriaResponseDTO findById(Integer id) {
        log.info("[CategoriaService] Buscando categoria id={}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[CategoriaService] Categoria no encontrada id={}", id);
                    return new RecursoNoEncontradoException(
                            "Categoria no encontrada con id: " + id);
                });
        return toResponseDTO(categoria, false);
    }


    public CategoriaResponseDTO save(CategoriaRequestDTO dto) {
        log.info("[CategoriaService] Creando categoria nombre={}", dto.getNombre());

        if (categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            log.warn("[CategoriaService] Nombre duplicado: {}", dto.getNombre());
            throw new ReglaDeNegocioException(
                    "Ya existe una categoria con el nombre: " + dto.getNombre());
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setActivo(true);

        Categoria guardada = categoriaRepository.save(categoria);
        log.info("[CategoriaService] Categoria creada id={}", guardada.getId());
        return toResponseDTO(guardada, false);
    }

    public CategoriaResponseDTO update(Integer id, CategoriaRequestDTO dto) {
        log.info("[CategoriaService] Actualizando categoria id={}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoria no encontrada con id: " + id));

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        Categoria actualizada = categoriaRepository.save(categoria);
        log.info("[CategoriaService] Categoria actualizada id={}", actualizada.getId());
        return toResponseDTO(actualizada, false);
    }

    public void delete(Integer id) {
        log.info("[CategoriaService] Eliminando categoria id={}", id);
        if (!categoriaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Categoria no encontrada con id: " + id);
        }
        categoriaRepository.deleteById(id);
        log.info("[CategoriaService] Categoria eliminada id={}", id);
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria, boolean conJuegos) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        dto.setActivo(categoria.getActivo());

        if (conJuegos) {
            try {
                List<JuegoClientDTO> juegos =
                        juegoClient.obtenerJuegosPorGenero(categoria.getNombre());
                dto.setJuegos(juegos.stream().map(j -> {
                    JuegoClientResponseDTO jDto = new JuegoClientResponseDTO();
                    jDto.setId(j.getId());
                    jDto.setTitulo(j.getTitulo());
                    jDto.setPrecio(j.getPrecio());
                    jDto.setDesarrollador(j.getDesarrollador());
                    jDto.setStock(j.getStock());
                    jDto.setActivo(j.getActivo());
                    return jDto;
                }).collect(Collectors.toList()));
                log.info("[CategoriaService] Juegos enriquecidos para categoria id={}",
                        categoria.getId());
            } catch (FeignException e) {
                log.warn("[CategoriaService] No se pudo obtener juegos para categoria id={}",
                        categoria.getId());
                dto.setJuegos(Collections.emptyList());
            }
        }

        return dto;
    }
}