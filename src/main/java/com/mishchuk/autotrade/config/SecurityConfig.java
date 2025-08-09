package com.mishchuk.autotrade.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenAuthFilter tokenAuthFilter;

    @Bean
    UserDetailsService emptyDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("No local users, only JWT tokens allowed!");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .securityContext(
                        securityContext ->
                                securityContext
                                        .requireExplicitSave(true)
                                        .securityContextRepository(
                                                new DelegatingSecurityContextRepository(
                                                        new RequestAttributeSecurityContextRepository(),
                                                        new HttpSessionSecurityContextRepository())))
                .authorizeHttpRequests(
                        requests ->
                                requests
                                        .requestMatchers(
                                                "/users/login",
                                                "/users/registration-confirm**",
                                                "/users/registration-complete",
                                                "/users/passwords/reset",
                                                "/users/passwords/reset-verify**",
                                                "/users/passwords/reset-complete",
                                                "/auth/**"
                                        )
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .cors(
                        cors ->
                                cors.configurationSource(
                                        _ -> {
                                            CorsConfiguration configuration = new CorsConfiguration();
                                            configuration.setAllowedOrigins(List.of("http://localhost:3000"));
                                            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                                            configuration.setAllowedHeaders(List.of("*"));
                                            configuration.setAllowCredentials(true); // якщо потрібні cookie/JWT через браузер
                                            return configuration;
                                        }))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}