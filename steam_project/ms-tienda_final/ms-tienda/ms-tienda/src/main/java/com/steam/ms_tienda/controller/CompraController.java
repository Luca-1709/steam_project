package com.steam.ms_tienda.controller;
import com.steam.ms_tienda.model.dto.CompraRequestDTO;
import com.steam.ms_tienda.model.dto.CompraResponseDTO;
import com.steam.ms_tienda.service.CompraService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/compras")
public class CompraController {

    private static final Logger log = LoggerFactory.getLogger(CompraController.class);

    @Autowired
    private CompraService compraService;

    @GetMapping
    public ResponseEntity<List<CompraResponseDTO>> listar() {
        log.info("[CompraController] GET /api/v1/compras");
        List<CompraResponseDTO> lista = compraService.findAll();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> buscar(@PathVariable Integer id) {
        log.info("[CompraController] GET /api/v1/compras/{}", id);
        return ResponseEntity.ok(compraService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CompraResponseDTO> comprar(
            @Valid @RequestBody CompraRequestDTO dto) {
        log.info("[CompraController] POST /api/v1/compras usuarioId={} juegoId={}",
                dto.getUsuarioId(), dto.getJuegoId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(compraService.realizarCompra(dto));
    }
}
