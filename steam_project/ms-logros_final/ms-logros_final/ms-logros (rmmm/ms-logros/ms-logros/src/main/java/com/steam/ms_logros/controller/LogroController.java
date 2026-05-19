package com.steam.ms_logros.controller;

import com.steam.ms_logros.model.dto.*;
import com.steam.ms_logros.service.LogroService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/logros")
public class LogroController {

    private static final Logger log = LoggerFactory.getLogger(LogroController.class);

    @Autowired
    private LogroService logroService;

    @GetMapping
    public ResponseEntity<List<LogroResponseDTO>> listar() {
        log.info("[LogroController] GET /api/v1/logros");
        List<LogroResponseDTO> lista = logroService.findAllLogros();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogroResponseDTO> buscar(@PathVariable Integer id) {
        log.info("[LogroController] GET /api/v1/logros/{}", id);
        return ResponseEntity.ok(logroService.findLogroById(id));
    }

    @PostMapping
    public ResponseEntity<LogroResponseDTO> crear(
            @Valid @RequestBody LogroRequestDTO dto) {
        log.info("[LogroController] POST /api/v1/logros nombre={}", dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(logroService.saveLogro(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        log.info("[LogroController] DELETE /api/v1/logros/{}", id);
        logroService.deleteLogro(id);
        return ResponseEntity.noContent().build();
    }

        
}