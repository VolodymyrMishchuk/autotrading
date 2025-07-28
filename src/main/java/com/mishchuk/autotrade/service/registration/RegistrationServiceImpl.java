package com.mishchuk.autotrade.service.registration;

import com.mishchuk.autotrade.controller.dto.UserCreateDto;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.enums.UserRole;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.repository.UserRepository;
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

    @Override
    @Transactional
    public UUID register(UserCreateDto dto) {
        UserEntity user = userMapper.toEntity(dto);

        user.setId(UUID.randomUUID());
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

        return emailVerificationService.confirmToken(token);
    }
}