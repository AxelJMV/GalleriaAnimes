package com.misanimes.animefavoritos.service;


import com.misanimes.animefavoritos.dto.UserRegisterDto;
import com.misanimes.animefavoritos.entity.Anime;
import com.misanimes.animefavoritos.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.misanimes.animefavoritos.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository , BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegisterDto userDto){
        if(userRepository.findByUsername(userDto.getUsername()).isPresent()){
            throw new RuntimeException("Usuario ya existe");
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

    public User updateUser(Long id, String email, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setEmail(email);

        // Solo encriptar si la contraseña ha cambiado
        if (!password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        return userRepository.save(user);
    }

    public boolean authenticateUser(String identifier, String rawPassword) {
        User user;
        if(identifier.contains("@")){ // si contiene un arroba es por que sera un correo
            user = userRepository.findByEmail(identifier).orElseThrow(() -> new RuntimeException("Correo no encontrado"));
        }else{ // Si no, es un username
            user = userRepository.findByUsername(identifier)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        return passwordEncoder.matches(rawPassword, user.getPassword());
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
