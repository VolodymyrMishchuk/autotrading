package com.mishchuk.autotrade.service.email;

import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.repository.EmailVerificationTokenRepository;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.EmailVerificationTokenEntity;
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

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${spring.email.verification.expiry-minutes:60}")
    private long tokenExpiryMinutes;

    @Value("${spring.email.verification.base-url}")
    private String baseVerificationUrl;

    @Override
    public void sendVerificationEmail(UserEntity user) {
        String token = UUID.randomUUID().toString();
        Instant now = Instant.now();

        EmailVerificationTokenEntity entity = EmailVerificationTokenEntity.builder()
                .token(token)
                .createdAt(now)
                .expiresAt(now.plus(tokenExpiryMinutes, ChronoUnit.MINUTES))
                .user(user)
                .build();

        tokenRepository.save(entity);

        String confirmationLink = baseVerificationUrl + "?token=" + token;

        String body = String.format("""
                Welcome to AutoTrading!

                To activate your account, click the link below:
                %s

                This link will expire in %d minutes.
                """, confirmationLink, tokenExpiryMinutes);

        emailService.sendEmail(
                user.getEmail(),
                "Confirm your registration",
                body
        );
    }

    @Override
    @Transactional
    public boolean confirmToken(String token) {
        Optional<EmailVerificationTokenEntity> optional = tokenRepository.findByToken(token);

        if (optional.isEmpty()) return false;

        EmailVerificationTokenEntity entity = optional.get();

        if (entity.getConfirmedAt() != null || entity.getExpiresAt().isBefore(Instant.now())) {
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
        tokenRepository.deleteAllByUser(user);

        sendVerificationEmail(user);
    }
}