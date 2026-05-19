package com.steam.ms_amigos.exception;

public class ReglaDeNegocioException extends RuntimeException {
    public ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }
}