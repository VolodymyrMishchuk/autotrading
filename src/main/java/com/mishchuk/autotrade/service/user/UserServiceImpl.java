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

        user.setPassword(passwordEncoder.encode(password)); // чи потрібно тут задавати пароль (чи краще було б null, або взагалі видалити)?
        user.setStatus(Status.INACTIVE);
        user.setCreatedAt(Instant.now());

        UUID token = UUID.randomUUID();
        user.setToken(token);

        userRepository.save(userMapper.toUserEntity(user));

        emailService.sendEmail(
                user.getEmail(),
                "Confirm registration",
                "Please confirm your registration by clicking the link below:\n"
                + frontendBaseUrl
                + "/users/registration-confirm?token="
                + token);

        log.info("User crested: {}", user);
    }

    @Override
    public User getUser(String id) {

        Optional<UserEntity> optionalUserEntity =
                userRepository.findById(UUID.fromString(id));

        if (optionalUserEntity.isPresent()) {
            return  userMapper.toUser(optionalUserEntity.get());
        }

        throw new UserNotFoundException("User with id " + id + " not found");
    }

    @Override
    public User getByToken(String token) {

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
        log.info("Updating user with id: {}", user.getId());

        Optional<UserEntity> optionalUserEntity =
                userRepository.findById(UUID.fromString(user.getId()));
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();

            userEntity.setFirstName(user.getFirstName());
            userEntity.setLastName(user.getLastName());
            userEntity.setBirthDay(user.getBirthDate());
            userEntity.setPhoneNumber(user.getPhoneNumber());
            userEntity.setEmail(user.getEmail());
            userEntity.setRole(user.getRole());
            userEntity.setStatus(user.getStatus());

            userRepository.save(userEntity);
        } else {
            throw new UserNotFoundException("User with id " + user.getId() + " not found");
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
    }

    @Override
    public User getAuthenticatedUser() {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return getUser(userId);
    }
}