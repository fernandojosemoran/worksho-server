package org.gdzdev.workshop.backend.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.UUID;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig {
    private final SecurityFilter securityFilter;

    @Value("${spring.profiles.active}")
    private String mode;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerUrl;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        String selectSwaggerUrl = this.mode.equals("dev")
                ? String.format("%s/index.html", this.swaggerUrl)
                : String.format("/%s/*", UUID.randomUUID());

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui.html/index.html").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/seeder/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/*").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/purchase").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/sales").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/categories").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/carts").hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/api/v1/products").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/sales").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/purchase").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/carts").hasRole("USER")

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/purchase").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/sales").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/carts").hasRole("USER")

                        .requestMatchers(HttpMethod.PUT, "/api/v1/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/purchase").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/sales").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/carts").hasRole("USER")
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

