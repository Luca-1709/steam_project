package com.steam.ms_biblioteca.controller;

import com.steam.ms_biblioteca.model.dto.BibliotecaRequestDTO;
import com.steam.ms_biblioteca.model.dto.BibliotecaResponseDTO;
import com.steam.ms_biblioteca.service.BibliotecaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/biblioteca")
public class BibliotecaController {

    private static final Logger log = LoggerFactory.getLogger(BibliotecaController.class);

    @Autowired
    private BibliotecaService bibliotecaService;

    @GetMapping
    public ResponseEntity<List<BibliotecaResponseDTO>> listar() {
        log.info("[BibliotecaController] GET /api/v1/biblioteca");
        List<BibliotecaResponseDTO> lista = bibliotecaService.findAll();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BibliotecaResponseDTO> buscar(@PathVariable Integer id) {
        log.info("[BibliotecaController] GET /api/v1/biblioteca/{}", id);
        return ResponseEntity.ok(bibliotecaService.findById(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<BibliotecaResponseDTO>> porUsuario(
            @PathVariable Integer usuarioId) {
        log.info("[BibliotecaController] GET /api/v1/biblioteca/usuario/{}", usuarioId);
        List<BibliotecaResponseDTO> lista = bibliotecaService.findByUsuarioId(usuarioId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<BibliotecaResponseDTO> agregar(
            @Valid @RequestBody BibliotecaRequestDTO dto) {
        log.info("[BibliotecaController] POST /api/v1/biblioteca usuarioId={} juegoId={}",
                dto.getUsuarioId(), dto.getJuegoId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bibliotecaService.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        log.info("[BibliotecaController] DELETE /api/v1/biblioteca/{}", id);
        bibliotecaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}