package com.steam.ms_categorias.controller;

import com.steam.ms_categorias.model.dto.*;
import com.steam.ms_categorias.service.CategoriaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private static final Logger log = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar() {
        log.info("[CategoriaController] GET /api/v1/categorias");
        List<CategoriaResponseDTO> lista = categoriaService.findAll();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscar(@PathVariable Integer id) {
        log.info("[CategoriaController] GET /api/v1/categorias/{}", id);
        return ResponseEntity.ok(categoriaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crear(
            @Valid @RequestBody CategoriaRequestDTO dto) {
        log.info("[CategoriaController] POST /api/v1/categorias nombre={}", dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequestDTO dto) {
        log.info("[CategoriaController] PUT /api/v1/categorias/{}", id);
        return ResponseEntity.ok(categoriaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        log.info("[CategoriaController] DELETE /api/v1/categorias/{}", id);
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}