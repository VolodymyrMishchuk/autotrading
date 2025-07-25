package com.mishchuk.autotrade.service.user;

import com.mishchuk.autotrade.exception.UserNotFoundException;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.email.EmailService;
import com.mishchuk.autotrade.service.model.Status;
import com.mishchuk.autotrade.service.model.User;
import lombok.RequiredArgsConstructor;
//import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
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

    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

    @Override
    public void createUser(User user) {

        log.info("Creating new user");

        UUID token = UUID.randomUUID();

        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setBirthDate(user.getBirthDate());
        user.setPhoneNumber(user.getPhoneNumber());
        user.setEmail(user.getEmail());
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
                + token);

        log.info("User created: {}", user);
    }

    @Override
    public User getUserById(String id) {

        Optional<UserEntity> optionalUserEntity =
                userRepository.findById(UUID.fromString(id));

        if (optionalUserEntity.isPresent()) {
            return  userMapper.toUser(optionalUserEntity.get());
        }

        throw new UserNotFoundException("User with id " + id + " not found");
    }

    @Override
    public User getUserByToken(String token) {

        Optional<UserEntity> optionalUserEntity =
                userRepository.findByToken(UUID.fromString(token));

        if (optionalUserEntity.isPresent()) {
            return  userMapper.toUser(optionalUserEntity.get());
        }

        throw new UserNotFoundException("User with token " + token + " not found");
    }

    @Override
    public List<User> getAllUsers() {

        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toUser)
                .toList();
    }

    @Override
    public void updateUser(User user) {

        // я тут не реалізовував логіку оновлення номера телефону, імейлу, пароля, статусу та ролі
        // мені це видалося якось не дуж сек'юрно
        // тре подумати над такою фічою з якоюсь складнішою логікою

        log.info("Updating user with id: {}", user.getId());

        Optional<UserEntity> optionalUserEntity =
                userRepository.findById(UUID.fromString(user.getId()));

        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();

            userEntity.setFirstName(user.getFirstName());
            userEntity.setLastName(user.getLastName());
            userEntity.setBirthDay(user.getBirthDate());
            userEntity.setUpdatedAt(Instant.now());

            userRepository.save(userEntity);

            log.info("Updated user with id: {}", user.getId());
        } else {
            throw new UserNotFoundException("User with id " + user.getId() + " not found");
        }
    }

    @Override
    public void deleteUser(String id) {

        if (userRepository.existsById(UUID.fromString(id))) {
            userRepository.deleteById(UUID.fromString(id));
        } else {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }

    @Override
    public void completeRegistration(String token) {

        Optional<UserEntity> optionalUserEntity =
                userRepository.findByToken(UUID.fromString(token));

        if (optionalUserEntity.isPresent()){
            UserEntity userEntity = optionalUserEntity.get();

            userEntity.setStatus(Status.valueOf("ACTIVE"));
            userEntity.setUpdatedAt(Instant.now());
            userEntity.setToken(null);

            userRepository.save(userEntity);
        } else {
            throw new UserNotFoundException("User with token " + token + " not found");
        }
    }

    @Override
    public User getAuthenticatedUser() {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return getUserById(userId);
    }

    // Потрібна допомога із реалізацією логіки

    public void updatePhoneNumberOfUser(User user) {

    }

    public void updateEmailOfUser(User user) {

    }

    public void updatePasswordOfUser(User user) {

    }

    public void updateRoleOfUser(User user) {

    }

    public void updateStatusOfUser(User user) {

    }
}