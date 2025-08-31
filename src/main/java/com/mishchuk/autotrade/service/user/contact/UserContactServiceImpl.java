package com.mishchuk.autotrade.service.user.contact;

import com.mishchuk.autotrade.enums.TokenChannel;
import com.mishchuk.autotrade.enums.TokenPurpose;
import com.mishchuk.autotrade.exception.DuplicateFieldException;
import com.mishchuk.autotrade.exception.UserNotFoundException;
import com.mishchuk.autotrade.repository.UserActionTokenRepository;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.UserActionTokenEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.email.EmailService;
import com.mishchuk.autotrade.service.user.support.AuthUserAccessor;
import com.mishchuk.autotrade.service.verify.VerifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserContactServiceImpl implements UserContactService {

    private final UserRepository userRepository;
    private final UserActionTokenRepository tokenRepository;
    private final VerifyService verifyService;
    private final AuthUserAccessor authUserAccessor;
    private final EmailService emailService;

    @Value("${spring.frontend.base-url}")
    private String frontendBaseUrl;

    @Value("${spring.reset.expiry-minutes:60}")
    private long resetTokenExpiryMinutes;

    // ===== phone =====

    @Override
    public void changePhoneNumber(UUID userId, String oldPhone, String newPhone) {
        UserEntity e = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        if (e.getPhoneNumber() != null && !e.getPhoneNumber().equals(oldPhone)) {
            throw new IllegalArgumentException("Old phone number is incorrect");
        }

        if (userRepository.existsByPhoneNumberAndIdNot(newPhone, userId)) {
            throw new DuplicateFieldException("Phone number is already in use");
        }

        e.setPhoneNumber(newPhone);
        e.setUpdatedAt(Instant.now());
        userRepository.save(e);
        log.info("Phone changed for user {}", userId);
    }

    @Override
    public void requestPhoneNumberReset(String email) {
        Optional<UserEntity> maybeUser = userRepository.findByEmailIgnoreCase(email);
        if (maybeUser.isEmpty()) {
            log.debug("Phone reset requested for non-existing email {}", email);
            return;
        }
        UserEntity user = maybeUser.get();
        tokenRepository.deleteAllByUserAndPurpose(user, TokenPurpose.PHONE_RESET);

        String token = java.util.UUID.randomUUID().toString();
        Instant now = Instant.now();

        tokenRepository.save(UserActionTokenEntity.builder()
                .token(token)
                .purpose(TokenPurpose.PHONE_RESET)
                .channel(TokenChannel.EMAIL)
                .createdAt(now)
                .expiresAt(now.plus(resetTokenExpiryMinutes, ChronoUnit.MINUTES))
                .user(user)
                .build());

        String link = frontendBaseUrl + "/users/phone/reset?token=" + token;
        String body = """
                You requested to reset your phone number.

                Click the link:
                %s

                Token will expire in %d minutes.
                """.formatted(link, resetTokenExpiryMinutes);

        emailService.sendEmail(
                user.getEmail(),
                "Phone number reset request",
                body
        );

        log.info("Phone reset token issued for {}", email);
    }

    @Override
    public void completePhoneNumberReset(String token, String newPhone) {
        UserActionTokenEntity t = tokenRepository.findByTokenAndPurpose(token, TokenPurpose.PHONE_RESET)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (t.getExpiresAt().isBefore(Instant.now())) {
            tokenRepository.delete(t);
            throw new IllegalArgumentException("Token expired");
        }

        UserEntity user = t.getUser();

        if (userRepository.existsByPhoneNumberAndIdNot(newPhone, user.getId())) {
            throw new DuplicateFieldException("Phone number is already in use");
        }

        user.setPhoneNumber(newPhone);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        tokenRepository.delete(t);
        log.info("Phone reset completed for user {}", user.getId());
    }

    // ===== email =====

    @Override
    public void changeEmail(UUID userId, String oldEmail, String newEmail) {
        UserEntity e = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        if (!e.getEmail().equalsIgnoreCase(oldEmail)) {
            throw new IllegalArgumentException("Old email is incorrect");
        }

        String normalized = newEmail == null ? null : newEmail.trim().toLowerCase();
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("Email is empty");
        }

        if (userRepository.existsByEmailIgnoreCaseAndIdNot(normalized, userId)) {
            throw new DuplicateFieldException("Email is already in use");
        }

        e.setEmail(normalized);
        e.setUpdatedAt(Instant.now());
        userRepository.save(e);
        log.info("Email changed for user {}", userId);
    }

    @Override
    public void requestEmailResetByPhone(String phoneNumber) {
        Optional<UserEntity> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        if (userOpt.isEmpty()) {
            log.debug("Email reset for non-existing phone {}", phoneNumber);
            return;
        }
        verifyService.start(phoneNumber, VerifyService.Channel.SMS);
        log.info("Started SMS verification for {}", phoneNumber);
    }

    @Override
    public void completeEmailResetBySms(String code, String newEmail) {
        UserEntity user = authUserAccessor.getAuthenticatedUserEntity();

        if (!verifyService.check(user.getPhoneNumber(), code)) {
            throw new IllegalArgumentException("Invalid/expired code");
        }

        String normalized = newEmail == null ? null : newEmail.trim().toLowerCase();
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("Email is empty");
        }

        if (userRepository.existsByEmailIgnoreCaseAndIdNot(normalized, user.getId())) {
            throw new DuplicateFieldException("Email is already in use");
        }

        user.setEmail(normalized);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        log.info("Email reset via SMS completed for user {}", user.getId());
    }
}