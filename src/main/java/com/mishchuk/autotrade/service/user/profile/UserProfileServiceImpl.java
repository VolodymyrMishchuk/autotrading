package com.mishchuk.autotrade.service.user.profile;

import com.mishchuk.autotrade.exception.UserNotFoundException;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.email.EmailVerificationService;
import com.mishchuk.autotrade.service.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    @Override
    public User getUserById(UUID id) {
        return userMapper.toUser(
                userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found")));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUser).toList();
    }

    @Override
    public void updateUser(User user) {
        UUID id = user.getId();
        UserEntity e = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        e.setFirstName(user.getFirstName());
        e.setLastName(user.getLastName());
        e.setBirthDate(LocalDate.from(user.getBirthDate()));
        e.setUpdatedAt(Instant.now());
        userRepository.save(e);
        log.info("Updated user profile: {}", id);
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
        log.info("Deleted user: {}", id);
    }
}