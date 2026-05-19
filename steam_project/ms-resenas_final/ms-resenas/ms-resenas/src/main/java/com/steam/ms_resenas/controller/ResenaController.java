package com.steam.ms_resenas.controller;

import com.steam.ms_resenas.model.dto.*;
import com.steam.ms_resenas.service.ResenaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/resenas")
public class ResenaController {

    private static final Logger log = LoggerFactory.getLogger(ResenaController.class);

    @Autowired
    private ResenaService resenaService;

    @GetMapping
    public ResponseEntity<List<ResenaResponseDTO>> listar() {
        log.info("[ResenaController] GET /api/v1/resenas");
        List<ResenaResponseDTO> lista = resenaService.findAll();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResenaResponseDTO> buscar(@PathVariable Integer id) {
        log.info("[ResenaController] GET /api/v1/resenas/{}", id);
        return ResponseEntity.ok(resenaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ResenaResponseDTO> crear(
            @Valid @RequestBody ResenaRequestDTO dto) {
        log.info("[ResenaController] POST /api/v1/resenas usuarioId={} juegoId={}",
                dto.getUsuarioId(), dto.getJuegoId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resenaService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResenaResponseDTO> actualizar(@PathVariable Integer id,@Valid @RequestBody ResenaRequestDTO dto) {
        log.info("[ResenaController] PUT /api/v1/resenas/{}", id);
        return ResponseEntity.ok(resenaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id){
        log.info("[ResenaController] DELETE (api/v1/resensa/{}", id);
        resenaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}