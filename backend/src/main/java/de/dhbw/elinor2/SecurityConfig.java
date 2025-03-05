package de.dhbw.elinor2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUserSetupFilter jwtUserSetupFilter) throws Exception
    {
        http
                .authorizeHttpRequests(auth ->  auth
                        .requestMatchers(
                                "/v3/api-docs/**", // API-Dokumentation
                                "/swagger-ui/**",  // Swagger UI
                                "/swagger-ui.html" // Swagger HTML-Seite
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer -> { })
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterAfter(jwtUserSetupFilter, BearerTokenAuthenticationFilter.class);

        return http.build();
    }
}
