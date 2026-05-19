package com.steam.ms_tienda.service;

import com.steam.ms_tienda.client.BibliotecaClient;
import com.steam.ms_tienda.client.JuegoClient;
import com.steam.ms_tienda.client.UsuarioClient;
import com.steam.ms_tienda.model.dto.BibliotecaClientDTO;
import com.steam.ms_tienda.model.dto.JuegoClientDTO;
import com.steam.ms_tienda.model.dto.UsuarioClientDTO;
import com.steam.ms_tienda.model.dto.CompraRequestDTO;
import com.steam.ms_tienda.model.dto.CompraResponseDTO;
import com.steam.ms_tienda.exception.RecursoNoEncontradoException;
import com.steam.ms_tienda.exception.ReglaDeNegocioException;
import com.steam.ms_tienda.model.Compra;
import com.steam.ms_tienda.repository.CompraRepository;
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
public class CompraService {

    private static final Logger log = LoggerFactory.getLogger(CompraService.class);

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private JuegoClient juegoClient;
    @Autowired
    private BibliotecaClient bibliotecaClient;

    public List<CompraResponseDTO> findAll() {
        log.info("[CompraService] Consultando todas las compras");
        return compraRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CompraResponseDTO findById(Integer id) {
        log.info("[CompraService] Buscando compra id={}", id);
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[CompraService] Compra no encontrada id={}", id);
                    return new RecursoNoEncontradoException("Compra no encontrada con id: " + id);
                });
        return toResponseDTO(compra);
    }

    public CompraResponseDTO realizarCompra(CompraRequestDTO dto) {
        log.info("[CompraService] Iniciando compra usuarioId={} juegoId={}",
                dto.getUsuarioId(), dto.getJuegoId());

        UsuarioClientDTO usuario = validarUsuarioExiste(dto.getUsuarioId());
        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new ReglaDeNegocioException(
                    "El usuario con id " + dto.getUsuarioId() + " no está activo");
        }

        JuegoClientDTO juego = validarJuegoDisponible(dto.getJuegoId());

        if (compraRepository.existsByUsuarioIdAndJuegoId(dto.getUsuarioId(), dto.getJuegoId())) {
            log.warn("[CompraService] Usuario id={} ya compró el juego id={}",
                    dto.getUsuarioId(), dto.getJuegoId());
            throw new ReglaDeNegocioException(
                    "El usuario ya adquirió el juego '" + juego.getTitulo() + "' anteriormente");
        }

        if (usuario.getSaldo().compareTo(juego.getPrecio()) < 0) {
            log.warn("[CompraService] Saldo insuficiente usuarioId={} saldo={} precio={}",
                    dto.getUsuarioId(), usuario.getSaldo(), juego.getPrecio());
            throw new ReglaDeNegocioException(
                    "Saldo insuficiente. Saldo actual: " + usuario.getSaldo()
                    + " | Precio del juego: " + juego.getPrecio());
        }

        Compra compra = new Compra();
        compra.setUsuarioId(dto.getUsuarioId());
        compra.setJuegoId(dto.getJuegoId());
        compra.setPrecioPagado(juego.getPrecio());
        compra.setFechaCompra(LocalDateTime.now());
        compra.setEstado("COMPLETADA");
        compra.setMetodoPago(dto.getMetodoPago());

        Compra guardada = compraRepository.save(compra);
        log.info("[CompraService] Compra registrada id={} precio={}", guardada.getId(), guardada.getPrecioPagado());

        try {
            juegoClient.descontarStock(dto.getJuegoId(), 1);
            log.info("[CompraService] Stock descontado juegoId={}", dto.getJuegoId());
        } catch (FeignException e) {
            log.error("[CompraService] Error al descontar stock juegoId={}: {}",
                    dto.getJuegoId(), e.getMessage());
        }

        try {
            BibliotecaClientDTO bibliotecaDTO = new BibliotecaClientDTO();
            bibliotecaDTO.setUsuarioId(dto.getUsuarioId());
            bibliotecaDTO.setJuegoId(dto.getJuegoId());
            bibliotecaDTO.setHorasJugadas(0);
            bibliotecaClient.agregarABiblioteca(bibliotecaDTO);
            log.info("[CompraService] Juego id={} agregado a biblioteca del usuario id={}",
                    dto.getJuegoId(), dto.getUsuarioId());
        } catch (FeignException e) {
            log.error("[CompraService] Error al agregar a biblioteca usuarioId={} juegoId={}: {}",
                    dto.getUsuarioId(), dto.getJuegoId(), e.getMessage());
        }

        return toResponseDTO(guardada);
    }


    private UsuarioClientDTO validarUsuarioExiste(Integer usuarioId) {
        try {
            UsuarioClientDTO usuario = usuarioClient.obtenerUsuarioPorId(usuarioId);
            log.info("[CompraService] Usuario id={} validado", usuarioId);
            return usuario;
        } catch (FeignException.NotFound e) {
            log.error("[CompraService] Usuario id={} no encontrado", usuarioId);
            throw new RecursoNoEncontradoException("Usuario no encontrado con id: " + usuarioId);
        } catch (FeignException e) {
            log.error("[CompraService] ms-usuarios no disponible: {}", e.getMessage());
            throw e;
        }
    }

    private JuegoClientDTO validarJuegoDisponible(Integer juegoId) {
        try {
            JuegoClientDTO juego = juegoClient.obtenerJuegoPorId(juegoId);

            if (Boolean.FALSE.equals(juego.getActivo())) {
                throw new ReglaDeNegocioException(
                        "El juego '" + juego.getTitulo() + "' no está disponible en la tienda");
            }
            if (juego.getStock() <= 0) {
                throw new ReglaDeNegocioException(
                        "El juego '" + juego.getTitulo() + "' no tiene stock disponible");
            }

            log.info("[CompraService] Juego id={} validado titulo={} precio={}",
                    juegoId, juego.getTitulo(), juego.getPrecio());
            return juego;

        } catch (ReglaDeNegocioException e) {
            throw e;
        } catch (FeignException.NotFound e) {
            log.error("[CompraService] Juego id={} no encontrado", juegoId);
            throw new RecursoNoEncontradoException("Juego no encontrado con id: " + juegoId);
        } catch (FeignException e) {
            log.error("[CompraService] ms-juegos no disponible: {}", e.getMessage());
            throw e;
        }
    }

    private CompraResponseDTO toResponseDTO(Compra compra) {
        CompraResponseDTO dto = new CompraResponseDTO();
        dto.setId(compra.getId());
        dto.setUsuarioId(compra.getUsuarioId());
        dto.setJuegoId(compra.getJuegoId());
        dto.setPrecioPagado(compra.getPrecioPagado());
        dto.setFechaCompra(compra.getFechaCompra());
        dto.setEstado(compra.getEstado());
        dto.setMetodoPago(compra.getMetodoPago());

        try {
            UsuarioClientDTO usuario = usuarioClient.obtenerUsuarioPorId(compra.getUsuarioId());
            dto.setUsernameUsuario(usuario.getUsername());
            dto.setNombreUsuario(usuario.getNombre() + " " + usuario.getApellido());
        } catch (FeignException e) {
            log.warn("[CompraService] No se pudo enriquecer con ms-usuarios usuarioId={}",
                    compra.getUsuarioId());
            dto.setUsernameUsuario("N/A");
            dto.setNombreUsuario("N/A");
        }

        try {
            JuegoClientDTO juego = juegoClient.obtenerJuegoPorId(compra.getJuegoId());
            dto.setTituloJuego(juego.getTitulo());
            dto.setGeneroJuego(juego.getGenero());
            dto.setDesarrolladorJuego(juego.getDesarrollador());
        } catch (FeignException e) {
            log.warn("[CompraService] No se pudo enriquecer con ms-juegos juegoId={}",
                    compra.getJuegoId());
            dto.setTituloJuego("N/A");
            dto.setGeneroJuego("N/A");
            dto.setDesarrolladorJuego("N/A");
        }

        return dto;
    }
}
