package com.steam.ms_resenas.exception;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(RecursoNoEncontradoException ex) {
        log.error("[GlobalExceptionHandler] 404 - {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ReglaDeNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleRegla(ReglaDeNegocioException ex) {
        log.warn("[GlobalExceptionHandler] 400 - Regla de negocio: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> erroresCampos = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo   = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            erroresCampos.put(campo, mensaje);
            log.warn("[GlobalExceptionHandler] Validacion fallida campo={} mensaje={}", campo, mensaje);
        });
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status",    HttpStatus.BAD_REQUEST.value());
        body.put("error",     "Error de validacion");
        body.put("errores",   erroresCampos);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeign(FeignException ex) {
        log.error("[GlobalExceptionHandler] Error Feign status={} mensaje={}", ex.status(), ex.getMessage());
        if (ex.status() == 404) {
            return buildError(HttpStatus.NOT_FOUND,
                    "El recurso solicitado no existe en el servicio externo");
        }
        return buildError(HttpStatus.SERVICE_UNAVAILABLE,
                "Un servicio externo no esta disponible. Intente mas tarde.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        log.error("[GlobalExceptionHandler] 500 - Error inesperado: {}", ex.getMessage());
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String mensaje) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status",    status.value());
        body.put("error",     status.getReasonPhrase());
        body.put("mensaje",   mensaje);
        return ResponseEntity.status(status).body(body);
    }
}
