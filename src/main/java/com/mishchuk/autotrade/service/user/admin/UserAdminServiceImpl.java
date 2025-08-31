package com.mishchuk.autotrade.service.user.admin;

import com.mishchuk.autotrade.exception.UserNotFoundException;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;

    @Override
    public void updateRoleOfUser(User user) {
        UUID id = user.getId();
        UserEntity e = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        e.setRole(user.getRole());
        e.setUpdatedAt(Instant.now());
        userRepository.save(e);
        log.info("Role updated for user {}", id);
    }

    @Override
    public void updateStatusOfUser(User user) {
        UUID id = user.getId();
        UserEntity e = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        e.setStatus(user.getStatus());
        e.setUpdatedAt(Instant.now());
        userRepository.save(e);
        log.info("Status updated for user {}", id);
    }
}