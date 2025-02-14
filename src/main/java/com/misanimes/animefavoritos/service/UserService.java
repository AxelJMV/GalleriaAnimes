package com.misanimes.animefavoritos.service;


import com.misanimes.animefavoritos.dto.UserRegisterDto;
import com.misanimes.animefavoritos.entity.Anime;
import com.misanimes.animefavoritos.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.misanimes.animefavoritos.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User registerUser(UserRegisterDto userDto){
        if(userRepository.findByUsername(userDto.getUsername()).isPresent()){
            throw new IllegalArgumentException("El nombre de usuario ya está registrado");
        }
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        // Crear entidad User desde el DTO
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Encriptar contraseña

        return userRepository.save(user);
    }


    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long id, String email,String username, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        //Validar que el username no este en uso por otro usuario
        userRepository.findByUsername(username)
                .ifPresent(existingUser -> {
                    if(!existingUser.getEmail().equals(email)){
                        throw new RuntimeException("El username ya está en uso por otro usuario");
                    }
                });

        // Validar si el nuevo email ya está en uso por otro usuario
        userRepository.findByEmail(email)
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) { // Asegurarse de que no sea el mismo usuario
                        throw new RuntimeException("El email ya está en uso por otro usuario");
                    }
                });

        // Actualizar el username y email
        user.setUsername(username);
        user.setEmail(email);

        // Solo encriptar y actualizar si la contraseña ha cambiado
        if (!password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        // Guardar los cambios en la base de datos
        return userRepository.save(user);
    }

//    public boolean authenticateUser(String identifier, String rawPassword) {
//        User user;
//        if(identifier.contains("@")){ // si contiene un arroba es por que sera un correo
//            user = userRepository.findByEmail(identifier).orElseThrow(() -> new RuntimeException("Correo no encontrado"));
//        }else{ // Si no, es un username
//            user = userRepository.findByUsername(identifier)
//                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//        }
//
//        return passwordEncoder.matches(rawPassword, user.getPassword());
//    }


    // Servicio para autenticar al usuario
    public Optional<User> authenticateUser(String identifier, String rawPassword) {
        User user;
        if (identifier.contains("@")) { // Si contiene '@', es un correo
            user = userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new RuntimeException("Correo no encontrado"));
        } else { // Si no, es un username
            user = userRepository.findByUsername(identifier)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        // Validar la contraseña
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return Optional.of(user); // Si es válido, retorna el usuario
        } else {
            return Optional.empty(); // Si no, retorna vacío
        }
    }


    public List<Anime> getUserAnimes(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Anime> animes = user.getAnimes();
        //validamos si la lista de animes esta vacio o llena
        if(animes.isEmpty()){
            throw new RuntimeException("El usuario no tienes animes registrados");
        }

        return animes;
    }
}
