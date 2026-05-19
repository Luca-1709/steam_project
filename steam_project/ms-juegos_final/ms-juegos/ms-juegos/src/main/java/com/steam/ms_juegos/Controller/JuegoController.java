package com.steam.ms_juegos.Controller;
import com.steam.ms_juegos.Model.dto.*;
import com.steam.ms_juegos.Service.JuegoService;
import jakarta.validation.Valid;
import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Data
@RestController
@RequestMapping("/api/v1/juegos")
public class JuegoController {
    private static final Logger log = LoggerFactory.getLogger(JuegoController.class);

    @Autowired
    private JuegoService juegoService;

    @GetMapping
    public ResponseEntity<List<JuegoResponseDTO>> listar() {
        log.info("[JuegoController] GET /api/v1/juegos");
        List<JuegoResponseDTO> lista = juegoService.findAll();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuegoResponseDTO> buscar(@PathVariable Integer id) {
        log.info("[JuegoController] GET /api/v1/juegos/{}", id);
        return ResponseEntity.ok(juegoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<JuegoResponseDTO> crear(
        @Valid @RequestBody JuegoRequestDTO dto) {
        log.info("[JuegoController] POST /api/v1/juegos - titulo={}", dto.getTitulo());
        return ResponseEntity.status(HttpStatus.CREATED).body(juegoService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JuegoResponseDTO> actualizar(
        @PathVariable Integer id,
        @Valid @RequestBody JuegoRequestDTO dto) {
        log.info("[JuegoController] PUT /api/v1/juegos/{}", id);
        return ResponseEntity.ok(juegoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        log.info("[JuegoController] DELETE /api/v1/juegos/{}", id);
        juegoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<JuegoResponseDTO> descontarStock(
            @PathVariable Integer id,
            @RequestParam Integer cantidad) {
        log.info("[JuegoController] PATCH /api/v1/juegos/{}/stock cantidad={}", id, cantidad);
        return ResponseEntity.ok(juegoService.descontarStock(id, cantidad));
    }
}
