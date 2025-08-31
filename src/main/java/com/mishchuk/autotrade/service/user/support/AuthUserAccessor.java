package com.mishchuk.autotrade.service.user.support;

import com.mishchuk.autotrade.exception.UserNotFoundException;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthUserAccessor {

    private final UserRepository userRepository;

    public UUID getAuthenticatedUserId() {
        String userIdStr = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return UUID.fromString(userIdStr);
    }

    public UserEntity getAuthenticatedUserEntity() {
        UUID userId = getAuthenticatedUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }
}