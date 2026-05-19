package com.steam.ms_juegos.Service;
import com.steam.ms_juegos.Model.dto.*;
import com.steam.ms_juegos.exception.*;
import com.steam.ms_juegos.Model.Juego;
import com.steam.ms_juegos.Repository.JuegoRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Data
public class JuegoService {
private static final Logger log = LoggerFactory.getLogger(JuegoService.class);

    @Autowired
    private JuegoRepository juegoRepository;

    public List<JuegoResponseDTO> findAll() {
        log.info("[JuegoService] Consultando todos los juegos");
        return juegoRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public JuegoResponseDTO findById(Integer id) {
        log.info("[JuegoService] Buscando juego id={}", id);
        Juego j = juegoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[JuegoService] Juego no encontrado id={}", id);
                    return new RecursoNoEncontradoException("Juego no encontrado con id: " + id);
                });
        return toResponseDTO(j);
    }

    public JuegoResponseDTO save(JuegoRequestDTO dto) {
        log.info("[JuegoService] Creando juego titulo={}", dto.getTitulo());
        if (juegoRepository.existsByTituloIgnoreCase(dto.getTitulo())) {
            log.warn("[JuegoService] Título duplicado: {}", dto.getTitulo());
            throw new ReglaDeNegocioException("Ya existe un juego con el título: " + dto.getTitulo());
        }

        Juego j = new Juego();
        j.setTitulo(dto.getTitulo());
        j.setDescripcion(dto.getDescripcion());
        j.setPrecio(dto.getPrecio());
        j.setFechaLanzamiento(dto.getFechaLanzamiento());
        j.setDesarrollador(dto.getDesarrollador());
        j.setGenero(dto.getGenero());
        j.setStock(dto.getStock());
        j.setActivo(true);

        Juego guardado = juegoRepository.save(j);
        log.info("[JuegoService] Juego creado exitosamente id={}", guardado.getId());
        return toResponseDTO(guardado);
    }

    public JuegoResponseDTO update(Integer id, JuegoRequestDTO dto) {
        log.info("[JuegoService] Actualizando juego id={}", id);
        Juego j = juegoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Juego no encontrado con id: " + id));

        j.setTitulo(dto.getTitulo());
        j.setDescripcion(dto.getDescripcion());
        j.setPrecio(dto.getPrecio());
        j.setGenero(dto.getGenero());
        j.setDesarrollador(dto.getDesarrollador());
        j.setStock(dto.getStock());

        Juego actualizado = juegoRepository.save(j);
        log.info("[JuegoService] Juego actualizado id={}", actualizado.getId());
        return toResponseDTO(actualizado);
    }

    public void delete(Integer id) {
        log.info("[JuegoService] Eliminando juego id={}", id);
        if (!juegoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Juego no encontrado con id: " + id);
        }
        juegoRepository.deleteById(id);
        log.info("[JuegoService] Juego eliminado id={}", id);
    }

    public JuegoResponseDTO descontarStock(Integer id, Integer cantidad) {
        log.info("[JuegoService] Descontando stock juego id={} cantidad={}", id, cantidad);
        Juego j = juegoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Juego no encontrado con id: " + id));
        if (j.getStock() < cantidad) {
            log.warn("[JuegoService] Stock insuficiente juego id={} stock={} solicitado={}", id, j.getStock(), cantidad);
            throw new ReglaDeNegocioException("Stock insuficiente para el juego: " + j.getTitulo()
                    + ". Disponible: " + j.getStock());
        }

        j.setStock(j.getStock() - cantidad);
        return toResponseDTO(juegoRepository.save(j));
    }

    private JuegoResponseDTO toResponseDTO(Juego j) {
        JuegoResponseDTO dto = new JuegoResponseDTO();
        dto.setId(j.getId());
        dto.setTitulo(j.getTitulo());
        dto.setDescripcion(j.getDescripcion());
        dto.setPrecio(j.getPrecio());
        dto.setFechaLanzamiento(j.getFechaLanzamiento());
        dto.setDesarrollador(j.getDesarrollador());
        dto.setGenero(j.getGenero());
        dto.setStock(j.getStock());
        dto.setActivo(j.getActivo());
        return dto;
    }
}
