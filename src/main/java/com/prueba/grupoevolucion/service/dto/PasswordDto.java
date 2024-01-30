package com.prueba.grupoevolucion.service.dto;
import lombok.Data;

@Data
public class PasswordDto {
    private String newPassword;
    private String oldPassword;
}
