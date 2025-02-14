package com.misanimes.animefavoritos.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String username;
    private String email;
    private String password; // Opcional en caso de no cambiar
}
