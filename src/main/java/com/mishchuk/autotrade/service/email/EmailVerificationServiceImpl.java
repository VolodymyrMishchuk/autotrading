package com.mishchuk.autotrade.service.email;

import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.enums.TokenChannel;
import com.mishchuk.autotrade.enums.TokenPurpose;
import com.mishchuk.autotrade.repository.UserActionTokenRepository;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.UserActionTokenEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final UserActionTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${spring.email.verification.expiry-minutes:60}")
    private long tokenExpiryMinutes;

    @Value("${spring.email.verification.base-url}")
    private String baseVerificationUrl;

    @Override
    public void sendVerificationEmail(UserEntity user) {
        tokenRepository.deleteAllByUserAndPurpose(user, TokenPurpose.EMAIL_VERIFICATION);

        String token = UUID.randomUUID().toString();
        Instant now = Instant.now();

        UserActionTokenEntity entity = UserActionTokenEntity.builder()
                .token(token)
                .purpose(TokenPurpose.EMAIL_VERIFICATION)
                .channel(TokenChannel.EMAIL)
                .createdAt(now)
                .expiresAt(now.plus(tokenExpiryMinutes, ChronoUnit.MINUTES))
                .user(user)
                .build();

        tokenRepository.save(entity);

        String confirmationLink = baseVerificationUrl + "?token=" + token;
        String userName = (user.getFirstName() != null && !user.getFirstName().isBlank())
                ? user.getFirstName()
                : "User";

        emailService.sendVerificationEmailHtml(
                user.getEmail(),
                "Підтвердіть реєстрацію в AutoTrading",
                userName,
                confirmationLink,
                tokenExpiryMinutes
        );
    }

    @Override
    @Transactional
    public boolean confirmToken(String token) {
        Optional<UserActionTokenEntity> optional =
                tokenRepository.findByTokenAndPurpose(token, TokenPurpose.EMAIL_VERIFICATION);

        if (optional.isEmpty()) return false;

        UserActionTokenEntity entity = optional.get();

        if (entity.getConsumedAt() != null || entity.getExpiresAt().isBefore(Instant.now())) {
            return false;
        }

        UserEntity user = entity.getUser();
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);

        tokenRepository.delete(entity);
        return true;
    }

    @Override
    @Transactional
    public void resendVerificationEmail(UserEntity user) {
        tokenRepository.deleteAllByUserAndPurpose(user, TokenPurpose.EMAIL_VERIFICATION);
        sendVerificationEmail(user);
    }
}
