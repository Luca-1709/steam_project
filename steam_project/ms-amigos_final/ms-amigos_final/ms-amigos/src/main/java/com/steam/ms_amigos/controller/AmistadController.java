package com.steam.ms_amigos.controller;


import com.steam.ms_amigos.model.dto.*;
import com.steam.ms_amigos.service.AmistadService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/amigos")
public class AmistadController {

    private static final Logger log = LoggerFactory.getLogger(AmistadController.class);

    @Autowired
    private AmistadService amistadService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AmistadResponseDTO>> listarAmigos(
            @PathVariable Integer usuarioId) {
        log.info("[AmistadController] GET /api/v1/amigos/usuario/{}", usuarioId);
        List<AmistadResponseDTO> lista = amistadService.findAmigosDeUsuario(usuarioId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmistadResponseDTO> buscar(@PathVariable Integer id) {
        log.info("[AmistadController] GET /api/v1/amigos/{}", id);
        return ResponseEntity.ok(amistadService.findById(id));
    }

    @PostMapping("/solicitud")
    public ResponseEntity<AmistadResponseDTO> enviarSolicitud(
            @Valid @RequestBody AmistadRequestDTO dto) {
        log.info("[AmistadController] POST /api/v1/amigos/solicitud usuarioId={} amigoId={}",
                dto.getUsuarioId(), dto.getAmigoId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(amistadService.enviarSolicitud(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        log.info("[AmistadController] DELETE /api/v1/amigos/{}", id);
        amistadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}