package de.dhbw.elinor2.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Instant;

@Configuration
@EnableWebSecurity
public class TestSecurityConfig
{
    @Bean
    @Primary
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()) // Alle Endpoints freigeben
                .csrf(AbstractHttpConfigurer::disable); // Falls CSRF aktiv ist, deaktivieren
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> Jwt.withTokenValue(token)
                .subject(DefaultUser.ID)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600)) // 1 Stunde gültig
                .claim("preferred_username", DefaultUser.USERNAME)
                .claim("given_name", DefaultUser.FIRST_NAME)
                .claim("family_name", DefaultUser.LAST_NAME)
                .headers(h -> h.put("alg", "HS256")) // Algorithmus-Header
                .build();
    }
}
