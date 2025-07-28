package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.service.login.LoginService;
import com.mishchuk.autotrade.service.registration.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final RegistrationService registrationService;

    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @Valid @RequestBody UserCreateDto dto,
            UriComponentsBuilder uriBuilder) {

        UUID userId = registrationService.register(dto);

        URI location = uriBuilder
                .path("/users/{id}")
                .buildAndExpand(userId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/signup/confirm")
    public ResponseEntity<Void> confirmRegistration(@Valid @RequestBody UserCompleteRegistrationDto dto) {
        boolean success = registrationService.confirmByEmailAndToken(dto.getEmail(), dto.getConfirmationToken());

        return success
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmTokenFromBrowser(@RequestParam("token") String token) {
        boolean confirmed = registrationService.confirmByToken(token);

        String redirectUrl = String.format("%s/login?confirmed=%s", frontendBaseUrl, confirmed);

        return ResponseEntity.status(302)
                .header("Location", redirectUrl)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenDto> login(@Valid @RequestBody AuthLoginDto dto) {
        String jwt = loginService.login(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(new AuthTokenDto(jwt));
    }
}