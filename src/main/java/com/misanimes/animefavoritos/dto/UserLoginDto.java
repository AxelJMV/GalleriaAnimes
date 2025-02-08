package com.misanimes.animefavoritos.dto;


import lombok.Data;

@Data
public class UserLoginDto {
    private String identifier; // Puede ser username o email
    private String password;
}
