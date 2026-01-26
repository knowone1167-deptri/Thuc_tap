package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // ===== SWAGGER =====
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // ===== PUBLIC AUTH =====
                        .requestMatchers("/api/auth/**").permitAll()

                        // ===== TASK PERMISSIONS =====

                        // GET tasks -> USER & MANAGER
                        .requestMatchers(HttpMethod.GET, "/api/tasks/**")
                        .hasAnyRole("USER", "MANAGER")

                        // CREATE task -> USER & MANAGER
                        .requestMatchers(HttpMethod.POST, "/api/tasks/**")
                        .hasAnyRole("USER", "MANAGER")

                        // UPDATE task -> MANAGER only
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/**")
                        .hasRole("MANAGER")

                        // DELETE task -> MANAGER only
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/**")
                        .hasRole("MANAGER")

                        // ===== PROJECT PERMISSIONS =====

                        .requestMatchers(HttpMethod.GET, "/api/projects/**")
                        .hasAnyRole("USER", "MANAGER")

                        .requestMatchers(HttpMethod.POST, "/api/projects/**")
                        .hasRole("MANAGER")

                        .requestMatchers(HttpMethod.PUT, "/api/projects/**")
                        .hasRole("MANAGER")

                        .requestMatchers(HttpMethod.DELETE, "/api/projects/**")
                        .hasRole("MANAGER")

                        // All other requests must login
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
// da hoan thanh security