
/*  Configuracion antes de agregar JWT para proteccion de endpoints
package com.misanimes.animefavoritos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF (solo para pruebas)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permite acceso libre a todos los endpoints
                )
                .formLogin(form -> form.disable()) // Desactiva formulario de login
                .httpBasic(httpBasic -> httpBasic.disable()); // Desactiva autenticación básica

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
*/






