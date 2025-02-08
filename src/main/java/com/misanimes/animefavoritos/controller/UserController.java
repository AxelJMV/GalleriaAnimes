package com.misanimes.animefavoritos.controller;


import com.misanimes.animefavoritos.dto.UserLoginDto;
import com.misanimes.animefavoritos.dto.UserRegisterDto;
import com.misanimes.animefavoritos.dto.UserResponseDto;
import com.misanimes.animefavoritos.entity.Anime;
import com.misanimes.animefavoritos.entity.User;
import com.misanimes.animefavoritos.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.misanimes.animefavoritos.service.UserService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;
    private final JwtService jwtService;

    // Inyección por constructor (RECOMENDADO)
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    //Endpoint para registrar usuarios
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto userDto) {
        try {
            User newUser = userService.registerUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser); // 201 Created
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict
        }
    }



    //Endpint para actualizar un usuario por su id
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user.getEmail(), user.getPassword());
    }


/*    //Endpoint para inciar sesion
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto loginDto) {


        boolean isAuthenticated = userService.authenticateUser(loginDto.getIdentifier(), loginDto.getPassword());

        if (isAuthenticated) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }*/


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto) {
        // 1. Autenticar al usuario (valida username/email y contraseña)
        Optional<User> userOptional = userService.authenticateUser(loginDto.getIdentifier(), loginDto.getPassword());

        if (userOptional.isPresent()) {
            // 2. Obtener el usuario autenticado
            User user = userOptional.get();

            // 3. Generar el token JWT
            String token = jwtService.generateToken(user.getUsername(), user.getId());

            // 4. Devolver el token en la respuesta
            return ResponseEntity.ok(token); // Token enviado como respuesta al frontend
        } else {
            // Si las credenciales son incorrectas
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
