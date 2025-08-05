package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.service.auth.AuthTokenManager;
import com.mishchuk.autotrade.service.auth.AuthTokenService;
import com.mishchuk.autotrade.service.email.EmailVerificationService;
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
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final RegistrationService registrationService;
    private final UserMapper userMapper;
    private final AuthTokenService authTokenService;
    private final AuthTokenManager authTokenManager;
    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;

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
    public ResponseEntity<AuthTokenResponseDto> login(@Valid @RequestBody AuthLoginDto dto) {
        AuthTokenResponseDto tokens = loginService.login(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthTokenResponseDto> refresh(@RequestBody RefreshTokenRequestDto dto) {
        // 1. Перевіряємо refresh token (чи існує у БД, чи не expired)
        var tokenOpt = authTokenManager.findByToken(dto.getRefreshToken());
        if (tokenOpt.isEmpty() || tokenOpt.get().getExpiryDate().isBefore(Instant.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var user = tokenOpt.get().getUser();
        // 2. Створюємо нові access і refresh
        String newAccess = authTokenService.createAccessToken(userMapper.toUser(user));
        String newRefresh = authTokenService.createRefreshToken();
        // 3. Видаляємо старий, зберігаємо новий refresh
        authTokenManager.deleteByToken(dto.getRefreshToken());
        authTokenManager.create(user, newRefresh);
        // 4. Повертаємо access + refresh
        return ResponseEntity.ok(
                AuthTokenResponseDto.builder()
                        .accessToken(newAccess)
                        .refreshToken(newRefresh)
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequestDto dto) {
        authTokenManager.deleteByToken(dto.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody ResendVerificationRequestDto dto) {
        var userOpt = userRepository.findByEmailIgnoreCase(dto.getEmail());
        if (userOpt.isEmpty()) {
            // Навмисно повертаємо 200, щоб не підказувати наявність email у системі
            return ResponseEntity.ok().build();
        }
        var user = userOpt.get();
        if (user.getStatus() == Status.ACTIVE) {
            return ResponseEntity.ok("Account is already activated");
        }
        emailVerificationService.resendVerificationEmail(user);
        return ResponseEntity.ok("Verification email resent");
    }
}