package com.steam.ms_amigos.client.dto;


import lombok.Data;

@Data
public class UsuarioClientDTO {
    private Integer id;
    private String  username;
    private String  nombre;
    private String  apellido;
    private Boolean activo;
}
