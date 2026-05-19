package com.steam.ms_pagos.controller;


import com.steam.ms_pagos.model.dto.*;
import com.steam.ms_pagos.service.PagoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private static final Logger log = LoggerFactory.getLogger(PagoController.class);

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> listar() {
        log.info("[PagoController] GET /api/v1/pagos");
        List<PagoResponseDTO> lista = pagoService.findAll();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> buscar(@PathVariable Integer id) {
        log.info("[PagoController] GET /api/v1/pagos/{}", id);
        return ResponseEntity.ok(pagoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> procesarPago(
            @Valid @RequestBody PagoRequestDTO dto) {
        log.info("[PagoController] POST /api/v1/pagos usuarioId={} monto={}",
                dto.getUsuarioId(), dto.getMonto());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagoService.procesarPago(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody PagoRequestDTO dto) {
        log.info("[PagoController] PUT /api/v1/pagos/{}", id);
        return ResponseEntity.ok(pagoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        log.info("[PagoController] DELETE /api/v1/pagos/{}", id);
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}