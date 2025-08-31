package com.mishchuk.autotrade.service.registration;

import com.mishchuk.autotrade.controller.dto.user.UserCreateDto;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.enums.UserRole;
import com.mishchuk.autotrade.enums.TokenPurpose;
import com.mishchuk.autotrade.exception.DuplicateFieldException;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.repository.UserActionTokenRepository;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.UserActionTokenEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.email.EmailVerificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final UserActionTokenRepository userActionTokenRepository;

    @Override
    @Transactional
    public UUID register(UserCreateDto dto) {

        if (userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new DuplicateFieldException("Email is already in use");
        }
        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new DuplicateFieldException("Phone number is already in use");
        }
        UserEntity user = userMapper.toUserEntity(dto);

        if (user.getEmail() != null) {
            user.setEmail(user.getEmail().trim().toLowerCase());
        }

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(UserRole.ROLE_USER);
        user.setStatus(Status.PENDING);
        user.setCreatedAt(Instant.now());

        userRepository.save(user);

        emailVerificationService.sendVerificationEmail(user);

        return user.getId();
    }

    @Override
    @Transactional
    public boolean confirmByToken(String token) {
        return emailVerificationService.confirmToken(token);
    }

    @Override
    @Transactional
    public boolean confirmByEmailAndToken(String email, String token) {
        Optional<UserEntity> userOpt = userRepository.findByEmailIgnoreCase(email);
        if (userOpt.isEmpty()) return false;

        UserEntity user = userOpt.get();
        if (user.getStatus() == Status.ACTIVE) return true;

        Optional<UserActionTokenEntity> tokOpt =
                userActionTokenRepository.findByTokenAndPurpose(token, TokenPurpose.EMAIL_VERIFICATION);

        if (tokOpt.isEmpty()) return false;

        UserActionTokenEntity tok = tokOpt.get();
        if (!tok.getUser().getId().equals(user.getId())) return false;
        if (tok.getExpiresAt().isBefore(Instant.now())) {
            userActionTokenRepository.delete(tok);
            return false;
        }

        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        userActionTokenRepository.delete(tok);

        return true;
    }
}