package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.auth.AuthLoginDto;
import com.mishchuk.autotrade.controller.dto.auth.AuthTokenResponseDto;
import com.mishchuk.autotrade.controller.dto.user.ResendVerificationRequestDto;
import com.mishchuk.autotrade.controller.dto.user.UserCompleteRegistrationDto;
import com.mishchuk.autotrade.controller.dto.user.UserCreateDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Slf4j
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

    @Value("${spring.frontend.base-url}")
    private String frontendBaseUrl;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    private static final Duration REFRESH_TTL = Duration.ofDays(7);

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
        log.info("Login request: email={}", dto.getEmail());

        AuthTokenResponseDto tokens = loginService.login(dto.getEmail(), dto.getPassword());

        ResponseCookie refreshCookie = buildRefreshCookie(tokens.getRefreshToken());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(AuthTokenResponseDto.builder()
                        .accessToken(tokens.getAccessToken())
                        .refreshToken(tokens.getRefreshToken())
                        .build());
    }

    @PostMapping("/refresh")
    @Transactional
    public ResponseEntity<AuthTokenResponseDto> refresh(
            @CookieValue(name = "refresh_token", required = false) String refreshFromCookie
    ) {
        if (refreshFromCookie == null || refreshFromCookie.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var tokenOpt = authTokenManager.findByToken(refreshFromCookie);
        if (tokenOpt.isEmpty() || tokenOpt.get().getExpiryDate().isBefore(Instant.now())) {
            // Був або прострочений, або відсутній — пробуємо прибрати і віддаємо 401
            try {
                authTokenManager.deleteByToken(refreshFromCookie);
            } catch (ObjectOptimisticLockingFailureException | EmptyResultDataAccessException ex) {
                log.debug("Refresh token already removed concurrently (ignore).");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = tokenOpt.get().getUser();
        String newAccess = authTokenService.createAccessToken(userMapper.toUser(user));
        String newRefresh = authTokenService.createRefreshToken();

        // rotate RT — видаляємо старий, але не валимося, якщо його вже видалив інший потік
        try {
            authTokenManager.deleteByToken(refreshFromCookie);
        } catch (ObjectOptimisticLockingFailureException | EmptyResultDataAccessException ex) {
            log.debug("Concurrent RT delete detected (ignore).");
        }
        authTokenManager.create(user, newRefresh);

        ResponseCookie refreshCookie = buildRefreshCookie(newRefresh);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(AuthTokenResponseDto.builder()
                        .accessToken(newAccess)
                        .refreshToken(newRefresh)
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refresh_token", required = false) String refreshFromCookie
    ) {
        if (refreshFromCookie != null && !refreshFromCookie.isBlank()) {
            try {
                authTokenManager.deleteByToken(refreshFromCookie);
            } catch (ObjectOptimisticLockingFailureException | EmptyResultDataAccessException ex) {
                log.debug("RT already removed on logout (ignore).");
            }
        }
        ResponseCookie cleared = clearRefreshCookie();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cleared.toString())
                .build();
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody ResendVerificationRequestDto dto) {
        var userOpt = userRepository.findByEmailIgnoreCase(dto.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        var user = userOpt.get();
        if (user.getStatus() == Status.ACTIVE) {
            return ResponseEntity.ok("Account is already activated");
        }
        emailVerificationService.resendVerificationEmail(user);
        return ResponseEntity.ok("Verification email resent");
    }

    private boolean isProd() {
        return "prod".equalsIgnoreCase(activeProfile) || "production".equalsIgnoreCase(activeProfile);
    }

    private ResponseCookie buildRefreshCookie(String token) {
        // Prod: SameSite=None; Secure=true; Dev: Lax+insecure
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .path("/")
                .maxAge(REFRESH_TTL);

        if (isProd()) {
            b = b.sameSite("None").secure(true);
        } else {
            b = b.sameSite("Lax").secure(false);
        }
        return b.build();
    }

    private ResponseCookie clearRefreshCookie() {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0);

        if (isProd()) {
            b = b.sameSite("None").secure(true);
        } else {
            b = b.sameSite("Lax").secure(false);
        }
        return b.build();
    }
}
