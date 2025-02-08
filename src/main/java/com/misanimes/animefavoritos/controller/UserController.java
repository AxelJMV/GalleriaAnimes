package com.misanimes.animefavoritos.controller;


import com.misanimes.animefavoritos.dto.UserLoginDto;
import com.misanimes.animefavoritos.dto.UserRegisterDto;
import com.misanimes.animefavoritos.entity.Anime;
import com.misanimes.animefavoritos.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.misanimes.animefavoritos.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    // Inyección por constructor (RECOMENDADO)
    public UserController(UserService userService) {
        this.userService = userService;
    }


    //Endpoint para registrar usuarios
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegisterDto userDto) {
        User newUser = userService.registerUser(userDto);
        return ResponseEntity.ok(newUser);
    }

    //Endpint para actualizar un usuario por su id
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user.getEmail(), user.getPassword());
    }


    //Endpoint para inciar sesion
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto loginDto) {


        boolean isAuthenticated = userService.authenticateUser(loginDto.getIdentifier(), loginDto.getPassword());

        if (isAuthenticated) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }


    //Endpint para obtener los animes de un usuario
    @GetMapping("/{id}/animes")
    public ResponseEntity<?> getUserAnimes(@PathVariable Long id){

        List<Anime> userAnimes = userService.getUserAnimes(id);

        if(userAnimes.isEmpty()){
            return ResponseEntity.ok("El usuario no tiene animes creados.");
        }

        return ResponseEntity.ok(userAnimes);
    }



}
