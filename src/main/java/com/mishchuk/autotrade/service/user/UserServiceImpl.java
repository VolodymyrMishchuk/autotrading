package com.mishchuk.autotrade.service.user;

import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.enums.TokenChannel;
import com.mishchuk.autotrade.enums.TokenPurpose;
import com.mishchuk.autotrade.exception.UserNotFoundException;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.repository.UserActionTokenRepository;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.UserActionTokenEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.email.EmailService;
import com.mishchuk.autotrade.service.email.EmailVerificationService;
import com.mishchuk.autotrade.service.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserActionTokenRepository tokenRepository;
    private final EmailVerificationService emailVerificationService;

    @Value("${spring.frontend.base-url}")
    private String frontendBaseUrl;

    @Value("${spring.reset.expiry-minutes:60}")
    private long resetTokenExpiryMinutes;

    @Override
    public void createUser(User user) {
        log.info("Creating new user");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.INACTIVE);
        user.setCreatedAt(Instant.now());

        UserEntity saved = userRepository.save(userMapper.toUserEntity(user));
        emailVerificationService.sendVerificationEmail(saved);

        log.info("User created: {}", user.getEmail());
    }

    @Override
    public User getUserById(UUID id) {
        return userMapper.toUser(
                userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"))
        );
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUser)
                .toList();
    }

    @Override
    public void updateUser(User user) {
        UUID id = (user.getId());

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + user.getId() + " not found"));

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setBirthDate(LocalDate.from(user.getBirthDate()));
        userEntity.setUpdatedAt(Instant.now());

        userRepository.save(userEntity);

        log.info("Updated user profile: {}", user.getId());
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }

        userRepository.deleteById(id);
        log.info("Deleted user: {}", id);
    }

    @Override
    public void updateRoleOfUser(User user) {
        UUID id = user.getId();
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + user.getId()));

        userEntity.setRole(user.getRole());
        userEntity.setUpdatedAt(Instant.now());

        userRepository.save(userEntity);
        log.info("Role updated for user {}", user.getId());
    }

    @Override
    public void updateStatusOfUser(User user) {
        UUID id = user.getId();
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + user.getId()));

        userEntity.setStatus(user.getStatus());
        userEntity.setUpdatedAt(Instant.now());

        userRepository.save(userEntity);
        log.info("Status updated for user {}", user.getId());
    }

    // ================== PASSWORD ==================

    @Override
    public void changePassword(UUID userId, String oldPassword, String newPassword) {
        UserEntity e = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        if (!passwordEncoder.matches(oldPassword, e.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        e.setPassword(passwordEncoder.encode(newPassword));
        e.setUpdatedAt(Instant.now());
        userRepository.save(e);
        log.info("Password changed for user {}", userId);
    }

    @Override
    public void requestPasswordReset(String email) {
        Optional<UserEntity> maybeUser = userRepository.findByEmailIgnoreCase(email);
        if (maybeUser.isEmpty()) { log.debug("Password reset requested for non-existing email {}", email); return; }

        UserEntity user = maybeUser.get();
        tokenRepository.deleteAllByUserAndPurpose(user, TokenPurpose.PASSWORD_RESET);

        String token = UUID.randomUUID().toString();
        Instant now = Instant.now();

        tokenRepository.save(UserActionTokenEntity.builder()
                .token(token)
                .purpose(TokenPurpose.PASSWORD_RESET)
                .channel(TokenChannel.EMAIL)
                .createdAt(now)
                .expiresAt(now.plus(resetTokenExpiryMinutes, ChronoUnit.MINUTES))
                .user(user)
                .build()
        );

        String link = frontendBaseUrl + "/users/password/reset?token=" + token;
        String body = """
                You requested a password reset.

                Use this token in the app or click the link:
                %s

                Token will expire in %d minutes.
                """.formatted(link, resetTokenExpiryMinutes);

        emailService.sendEmail(email, "Password reset", body);
        log.info("Password reset token issued for {}", email);
    }

    @Override
    public void completePasswordReset(String token, String newPassword) {
        UserActionTokenEntity t = tokenRepository.findByTokenAndPurpose(token, TokenPurpose.PASSWORD_RESET)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        if (t.getExpiresAt().isBefore(Instant.now())) {
            tokenRepository.delete(t);
            throw new IllegalArgumentException("Token expired");
        }
        UserEntity user = t.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        tokenRepository.delete(t);
        log.info("Password reset completed for user {}", user.getId());
    }

    // ================== PHONE ==================

    @Override
    public void changePhoneNumber(UUID userId, String oldPhone, String newPhone) {
        UserEntity e = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        if (e.getPhoneNumber() != null && !e.getPhoneNumber().equals(oldPhone)) {
            throw new IllegalArgumentException("Old phone number is incorrect");
        }
        e.setPhoneNumber(newPhone);
        e.setUpdatedAt(Instant.now());
        userRepository.save(e);
        log.info("Phone changed for user {}", userId);
    }

    @Override
    public void requestPhoneNumberReset(String email) {
        Optional<UserEntity> maybeUser = userRepository.findByEmailIgnoreCase(email);
        if (maybeUser.isEmpty()) { log.debug("Phone reset requested for non-existing email {}", email); return; }

        UserEntity user = maybeUser.get();
        tokenRepository.deleteAllByUserAndPurpose(user, TokenPurpose.PHONE_RESET);

        String token = UUID.randomUUID().toString();
        Instant now = Instant.now();

        tokenRepository.save(UserActionTokenEntity.builder()
                .token(token)
                .purpose(TokenPurpose.PHONE_RESET)
                .channel(TokenChannel.EMAIL)
                .createdAt(now)
                .expiresAt(now.plus(resetTokenExpiryMinutes, ChronoUnit.MINUTES))
                .user(user)
                .build()
        );

        String link = frontendBaseUrl + "/users/phone/reset?token=" + token;
        String body = """
                You requested to reset your phone number.

                Use this token in the app or click the link:
                %s

                Token will expire in %d minutes.
                """.formatted(link, resetTokenExpiryMinutes);

        emailService.sendEmail(email, "Phone number reset", body);
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
        user.setPhoneNumber(newPhone);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        tokenRepository.delete(t);
        log.info("Phone reset completed for user {}", user.getId());
    }

    // ================== EMAIL ==================

    @Override
    public void changeEmail(UUID userId, String oldEmail, String newEmail) {
        UserEntity e = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        if (!e.getEmail().equalsIgnoreCase(oldEmail)) {
            throw new IllegalArgumentException("Old email is incorrect");
        }
        e.setEmail(newEmail);
        e.setUpdatedAt(Instant.now());
        userRepository.save(e);
        log.info("Email changed for user {}", userId);
    }

    @Override
    public void requestEmailResetByPhone(String phoneNumber) {
        // поки немає СМС-провайдера — залишимо заглушку
        throw new UnsupportedOperationException("SMS verification flow is not implemented yet");
    }

    @Override
    public void completeEmailResetBySms(String verificationCodeBySMS, String newEmail) {
        // поки немає СМС-провайдера — залишимо заглушку
        throw new UnsupportedOperationException("SMS verification flow is not implemented yet");
    }
}