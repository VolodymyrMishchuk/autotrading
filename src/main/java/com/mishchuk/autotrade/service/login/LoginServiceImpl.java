package com.mishchuk.autotrade.service.login;

import com.mishchuk.autotrade.controller.dto.auth.AuthTokenResponseDto;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.exception.PasswordIncorrectException;
import com.mishchuk.autotrade.exception.UserIsBlockedException;
import com.mishchuk.autotrade.exception.UserNotFoundException;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.auth.AuthTokenManager;
import com.mishchuk.autotrade.service.auth.AuthTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;
    private final AuthTokenManager authTokenManager;

    @Override
    @Transactional
    public AuthTokenResponseDto login(String email, String rawPassword) {
        UserEntity userEntity = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User with " + email + " not found"));

        // 1) Перевіряємо, що в БД є хеш пароля
        if (userEntity.getPassword() == null || userEntity.getPassword().isBlank()) {
            log.warn("User {} has empty encoded password in DB", email);
            throw new PasswordIncorrectException("Password was incorrect for user with email " + email);
        }

        // 2) Перевіряємо пароль по хешу з ентіті
        if (!passwordEncoder.matches(rawPassword, userEntity.getPassword())) {
            log.warn("Password is incorrect for {}", email);
            throw new PasswordIncorrectException("Password was incorrect for user with email " + email);
        }

        // 3) Перевіряємо статус
        if (userEntity.getStatus() == Status.BLOCKED) {
            log.warn("User with email {} is blocked", email);
            throw new UserIsBlockedException("User is blocked");
        }
        if (userEntity.getStatus() != Status.ACTIVE) {
            log.warn("User with email {} is not activated", email);
            throw new UserIsBlockedException("User is not active");
        }

        // 4) Створюємо токени (для access мапимо у сервісну модель без пароля)
        var userForToken = userMapper.toUser(userEntity);
        String accessToken = authTokenService.createAccessToken(userForToken);
        String refreshToken = authTokenService.createRefreshToken();

        // 5) Зберігаємо refresh токен
        authTokenManager.create(userEntity, refreshToken);

        return AuthTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
