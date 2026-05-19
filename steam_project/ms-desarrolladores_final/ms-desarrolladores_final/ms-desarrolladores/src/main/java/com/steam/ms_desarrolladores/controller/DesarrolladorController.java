package com.steam.ms_desarrolladores.controller;

import com.steam.ms_desarrolladores.model.dto.*;
import com.steam.ms_desarrolladores.service.DesarrolladorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/desarrolladores")
public class DesarrolladorController {

    private static final Logger log = LoggerFactory.getLogger(DesarrolladorController.class);

    @Autowired
    private DesarrolladorService desarrolladorService;

    @GetMapping
    public ResponseEntity<List<DesarrolladorResponseDTO>> listar() {
        log.info("[DesarrolladorController] GET /api/v1/desarrolladores");
        List<DesarrolladorResponseDTO> lista = desarrolladorService.findAll();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesarrolladorResponseDTO> buscar(@PathVariable Integer id) {
        log.info("[DesarrolladorController] GET /api/v1/desarrolladores/{}", id);
        return ResponseEntity.ok(desarrolladorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DesarrolladorResponseDTO> crear(
            @Valid @RequestBody DesarrolladorRequestDTO dto) {
        log.info("[DesarrolladorController] POST /api/v1/desarrolladores nombre={}",
                dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(desarrolladorService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DesarrolladorResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody DesarrolladorRequestDTO dto) {
        log.info("[DesarrolladorController] PUT /api/v1/desarrolladores/{}", id);
        return ResponseEntity.ok(desarrolladorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        log.info("[DesarrolladorController] DELETE /api/v1/desarrolladores/{}", id);
        desarrolladorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}