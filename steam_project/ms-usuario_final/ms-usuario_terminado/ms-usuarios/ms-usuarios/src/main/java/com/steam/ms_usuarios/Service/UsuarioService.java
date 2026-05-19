package com.steam.ms_usuarios.Service;

import com.steam.ms_usuarios.Model.UsuarioModel;
import com.steam.ms_usuarios.Model.dto.UsuarioRequestDTO;
import com.steam.ms_usuarios.Model.dto.UsuarioResponseDTO;
import com.steam.ms_usuarios.Repository.UsuarioRepository;
import com.steam.ms_usuarios.exception.RecursoNoEncontradoException;
import com.steam.ms_usuarios.exception.ReglaDeNegocioException;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class UsuarioService {
 private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioResponseDTO> findAll() {
        log.info("[UsuarioService] Consultando todos los usuarios");
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO findById(Integer id) {
        log.info("[UsuarioService] Buscando usuario id={}", id);
        UsuarioModel u = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[UsuarioService] Usuario no encontrado id={}", id);
                    return new RecursoNoEncontradoException("Usuario no encontrado con id: " + id);
                });
        return toResponseDTO(u);
    }

    public UsuarioResponseDTO save(UsuarioRequestDTO dto) {
        log.info("[UsuarioService] Creando usuario username={}", dto.getUsername());

        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            log.warn("[UsuarioService] Username duplicado: {}", dto.getUsername());
            throw new ReglaDeNegocioException("El username '" + dto.getUsername() + "' ya está en uso");
        }

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            log.warn("[UsuarioService] Email duplicado: {}", dto.getEmail());
            throw new ReglaDeNegocioException("El email '" + dto.getEmail() + "' ya está registrado");
        }

        UsuarioModel u = new UsuarioModel();
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        u.setNombre(dto.getNombre());
        u.setApellido(dto.getApellido());
        u.setPais(dto.getPais());
        u.setSaldo(BigDecimal.ZERO);         
        u.setFechaRegistro(LocalDate.now()); 
        u.setActivo(true);

        UsuarioModel guardado = usuarioRepository.save(u);
        log.info("[UsuarioService] Usuario creado exitosamente id={}", guardado.getId());
        return toResponseDTO(guardado);
    }

    public UsuarioResponseDTO update(Integer id, UsuarioRequestDTO dto) {
        log.info("[UsuarioService] Actualizando usuario id={}", id);
        UsuarioModel u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
        u.setNombre(dto.getNombre());
        u.setApellido(dto.getApellido());
        u.setPais(dto.getPais());

        UsuarioModel actualizado = usuarioRepository.save(u);
        log.info("[UsuarioService] Usuario actualizado id={}", actualizado.getId());
        return toResponseDTO(actualizado);
    }

    public void delete(Integer id) {
        log.info("[UsuarioService] Eliminando usuario id={}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
        log.info("[UsuarioService] Usuario eliminado id={}", id);
    }
 
    private UsuarioResponseDTO toResponseDTO(UsuarioModel u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setNombre(u.getNombre());
        dto.setApellido(u.getApellido());
        dto.setPais(u.getPais());
        dto.setSaldo(u.getSaldo());
        dto.setFechaRegistro(u.getFechaRegistro());
        dto.setActivo(u.getActivo());
        return dto;
    }
}

