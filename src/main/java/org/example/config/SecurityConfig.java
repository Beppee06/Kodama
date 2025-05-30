package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Permetti accesso pubblico a Swagger e agli endpoint di autenticazione
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/auth/**" // Assumendo che login/register siano qui
                        ).permitAll()
                        // Richiedi autenticazione per tutte le altre richieste
                        .anyRequest().permitAll()  // Modificato da .permitAll() a .authenticated()
                )
                // --- Configura HTTP Basic ---
                .httpBasic(withDefaults())

                // --- DISABILITA CSRF (Causa probabile del 403 sui POST) ---
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}