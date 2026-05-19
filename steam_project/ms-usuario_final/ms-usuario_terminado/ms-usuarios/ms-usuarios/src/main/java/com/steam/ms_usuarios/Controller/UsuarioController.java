package com.steam.ms_usuarios.Controller;
import com.steam.ms_usuarios.Model.dto.UsuarioRequestDTO;
import com.steam.ms_usuarios.Model.dto.UsuarioResponseDTO;
import com.steam.ms_usuarios.Service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
     private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        log.info("[UsuarioController] GET /api/v1/usuarios");
        List<UsuarioResponseDTO> lista = usuarioService.findAll();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable Integer id) {
        log.info("[UsuarioController] GET /api/v1/usuarios/{}", id);
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        log.info("[UsuarioController] POST /api/v1/usuarios - username={}", dto.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequestDTO dto) {
        log.info("[UsuarioController] PUT /api/v1/usuarios/{}", id);
        return ResponseEntity.ok(usuarioService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        log.info("[UsuarioController] DELETE /api/v1/usuarios/{}", id);
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
