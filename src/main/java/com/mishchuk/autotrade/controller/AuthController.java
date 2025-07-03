package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ===== SIGN UP (ініціює створення та відправку токена) =====
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @Valid @RequestBody UserCreateDto dto,
            UriComponentsBuilder uriBuilder) {

        UUID userId = authService.register(dto);
        URI location = uriBuilder
                .path("/users/{id}")
                .buildAndExpand(userId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // ===== COMPLETE REGISTRATION (підтвердження email) =====
    @PostMapping("/signup/confirm")
    public ResponseEntity<Void> confirmRegistration(
            @Valid @RequestBody UserCompleteRegistrationDto dto) {

        authService.confirmRegistration(dto.getEmail(), dto.getConfirmationToken());
        return ResponseEntity.ok().build();
    }

    // ===== LOGIN =====
    @PostMapping("/login")
    public ResponseEntity<AuthTokenDto> login(@RequestBody AuthLoginDto authLoginDto) {
        String token = authService.login(authLoginDto.getEmail(), authLoginDto.getPassword());
        return new ResponseEntity<>(new AuthTokenDto(token), HttpStatus.OK);
    }
}


