package com.misanimes.animefavoritos.controller;


import com.misanimes.animefavoritos.dto.UserLoginDto;
import com.misanimes.animefavoritos.dto.UserRegisterDto;
import com.misanimes.animefavoritos.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.misanimes.animefavoritos.service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    // Inyección por constructor (RECOMENDADO)
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegisterDto userDto) {
        User newUser = userService.registerUser(userDto);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user.getEmail(), user.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto loginDto) {


        boolean isAuthenticated = userService.authenticateUser(loginDto.getIdentifier(), loginDto.getPassword());

        if (isAuthenticated) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }


}
