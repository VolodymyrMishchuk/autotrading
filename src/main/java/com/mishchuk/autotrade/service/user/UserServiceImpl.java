package com.mishchuk.autotrade.service.user;

import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.exception.UserNotFoundException;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.mapper.UserUpdateMapper;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.email.EmailService;
import com.mishchuk.autotrade.service.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.frontend.base-url}")
    private String frontendBaseUrl;

    @Override
    public void createUser(User user) {
        log.info("Creating new user");

        UUID token = UUID.randomUUID();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.INACTIVE);
        user.setCreatedAt(Instant.now());
        user.setToken(token);

        userRepository.save(userMapper.toUserEntity(user));

        emailService.sendEmail(
                user.getEmail(),
                "Confirm registration",
                "Please confirm your registration by clicking the link below:\n"
                        + frontendBaseUrl
                        + "/users/registration-confirm?token="
                        + token
        );

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
    public User getUserByToken(String token) {
        return userMapper.toUser(
                userRepository.findByToken(UUID.fromString(token))
                        .orElseThrow(() -> new UserNotFoundException("User with token " + token + " not found"))
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
    public void completeRegistration(String token) {
        UserEntity userEntity = userRepository.findByToken(UUID.fromString(token))
                .orElseThrow(() -> new UserNotFoundException("User with token " + token + " not found"));

        userEntity.setStatus(Status.ACTIVE);
        userEntity.setUpdatedAt(Instant.now());
        userEntity.setToken(null);

        userRepository.save(userEntity);
        log.info("Completed registration for user: {}", userEntity.getEmail());
    }

    @Override
    public User getAuthenticatedUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserById(UUID.fromString(userId));
    }

    @Override
    public void updatePhoneNumberOfUser(User user) {
        UUID id = user.getId();
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + user.getId()));

        userEntity.setPhoneNumber(user.getPhoneNumber());
        userEntity.setUpdatedAt(Instant.now());

        userRepository.save(userEntity);
        log.info("Phone number updated for user {}", user.getId());
    }

    @Override
    public void updateEmailOfUser(User user) {
        UUID id = user.getId();
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + user.getId()));

        userEntity.setEmail(user.getEmail());
        userEntity.setUpdatedAt(Instant.now());

        userRepository.save(userEntity);
        log.info("Email updated for user {}", user.getId());
    }

    @Override
    public void updatePasswordOfUser(User user) {
        UUID id = user.getId();
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + user.getId()));

        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setUpdatedAt(Instant.now());

        userRepository.save(userEntity);
        log.info("Password updated for user {}", user.getId());
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
}