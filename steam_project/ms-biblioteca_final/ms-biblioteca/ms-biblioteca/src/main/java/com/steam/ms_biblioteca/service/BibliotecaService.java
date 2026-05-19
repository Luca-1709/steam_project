package com.steam.ms_biblioteca.service;

import com.steam.ms_biblioteca.client.JuegoClient;
import com.steam.ms_biblioteca.client.UsuarioClient;
import com.steam.ms_biblioteca.model.dto.JuegoClientDTO;
import com.steam.ms_biblioteca.model.dto.UsuarioClientDTO;
import com.steam.ms_biblioteca.model.dto.BibliotecaRequestDTO;
import com.steam.ms_biblioteca.model.dto.BibliotecaResponseDTO;
import com.steam.ms_biblioteca.exception.RecursoNoEncontradoException;
import com.steam.ms_biblioteca.model.BibliotecaModel;
import com.steam.ms_biblioteca.repository.BibliotecaRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BibliotecaService {

    private static final Logger log = LoggerFactory.getLogger(BibliotecaService.class);

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private JuegoClient juegoClient;

    public List<BibliotecaResponseDTO> findAll() {
        log.info("[BibliotecaService] Consultando todas las entradas de biblioteca");
        return bibliotecaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public BibliotecaResponseDTO findById(Integer id) {
        log.info("[BibliotecaService] Buscando entrada id={}", id);
        BibliotecaModel entrada = bibliotecaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[BibliotecaService] Entrada no encontrada id={}", id);
                    return new RecursoNoEncontradoException(
                            "Entrada de biblioteca no encontrada con id: " + id);
                });
        return toResponseDTO(entrada);
    }

    public List<BibliotecaResponseDTO> findByUsuarioId(Integer usuarioId) {
        log.info("[BibliotecaService] Consultando biblioteca del usuario id={}", usuarioId);
        validarUsuarioExiste(usuarioId);
        return bibliotecaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public BibliotecaResponseDTO save(BibliotecaRequestDTO dto) {
        log.info("[BibliotecaService] Agregando juego id={} a la biblioteca del usuario id={}",
                dto.getJuegoId(), dto.getUsuarioId());


        validarUsuarioExiste(dto.getUsuarioId());

        validarJuegoExiste(dto.getJuegoId());


        BibliotecaModel entrada = new BibliotecaModel();
        entrada.setUsuarioId(dto.getUsuarioId());
        entrada.setJuegoId(dto.getJuegoId());
        entrada.setFechaAdquisicion(LocalDate.now());
        entrada.setHorasJugadas(dto.getHorasJugadas() != null ? dto.getHorasJugadas() : 0);
        entrada.setActivo(true);

        BibliotecaModel guardada = bibliotecaRepository.save(entrada);
        log.info("[BibliotecaService] Entrada creada exitosamente id={}", guardada.getId());
        return toResponseDTO(guardada);
    }

    public void delete(Integer id) {
        log.info("[BibliotecaService] Eliminando entrada id={}", id);
        if (!bibliotecaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Entrada de biblioteca no encontrada con id: " + id);
        }
        bibliotecaRepository.deleteById(id);
        log.info("[BibliotecaService] Entrada eliminada id={}", id);
    }


    private void validarUsuarioExiste(Integer usuarioId) {
        try {
            usuarioClient.obtenerUsuarioPorId(usuarioId);
            log.info("[BibliotecaService] Usuario id={} validado en ms-usuarios", usuarioId);
        } catch (FeignException.NotFound e) {
            log.error("[BibliotecaService] Usuario id={} no encontrado en ms-usuarios", usuarioId);
            throw new RecursoNoEncontradoException("Usuario no encontrado con id: " + usuarioId);
        } catch (FeignException e) {
            log.error("[BibliotecaService] ms-usuarios no disponible: {}", e.getMessage());
            throw e; // el GlobalExceptionHandler lo captura como 503
        }
    }

    private void validarJuegoExiste(Integer juegoId) {
        try {
            juegoClient.obtenerJuegoPorId(juegoId);
            log.info("[BibliotecaService] Juego id={} validado en ms-juegos", juegoId);
        } catch (FeignException.NotFound e) {
            log.error("[BibliotecaService] Juego id={} no encontrado en ms-juegos", juegoId);
            throw new RecursoNoEncontradoException("Juego no encontrado con id: " + juegoId);
        } catch (FeignException e) {
            log.error("[BibliotecaService] ms-juegos no disponible: {}", e.getMessage());
            throw e;
        }
    }

    private BibliotecaResponseDTO toResponseDTO(BibliotecaModel entrada) {
        BibliotecaResponseDTO dto = new BibliotecaResponseDTO();
        dto.setId(entrada.getId());
        dto.setUsuarioId(entrada.getUsuarioId());
        dto.setJuegoId(entrada.getJuegoId());
        dto.setFechaAdquisicion(entrada.getFechaAdquisicion());
        dto.setHorasJugadas(entrada.getHorasJugadas());
        dto.setActivo(entrada.getActivo());


        try {
            UsuarioClientDTO usuario = usuarioClient.obtenerUsuarioPorId(entrada.getUsuarioId());
            dto.setUsernameUsuario(usuario.getUsername());
            dto.setNombreUsuario(usuario.getNombre() + " " + usuario.getApellido());
        } catch (FeignException e) {
            log.warn("[BibliotecaService] No se pudo enriquecer con ms-usuarios para usuarioId={}",
                    entrada.getUsuarioId());
            dto.setUsernameUsuario("N/A");
            dto.setNombreUsuario("N/A");
        }

        try {
            JuegoClientDTO juego = juegoClient.obtenerJuegoPorId(entrada.getJuegoId());
            dto.setTituloJuego(juego.getTitulo());
            dto.setGeneroJuego(juego.getGenero());
            dto.setPrecioJuego(juego.getPrecio());
            dto.setDesarrolladorJuego(juego.getDesarrollador());
        } catch (FeignException e) {
            log.warn("[BibliotecaService] No se pudo enriquecer con ms-juegos para juegoId={}",
                    entrada.getJuegoId());
            dto.setTituloJuego("N/A");
            dto.setGeneroJuego("N/A");
            dto.setDesarrolladorJuego("N/A");
        }

        return dto;
    }
}
