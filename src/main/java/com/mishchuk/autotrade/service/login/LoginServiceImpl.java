package com.mishchuk.autotrade.service.login;

import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.exception.PasswordIncorrectException;
import com.mishchuk.autotrade.exception.UserIsBlockedException;
import com.mishchuk.autotrade.exception.UserNotFoundException;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.service.auth.AuthTokenService;
import com.mishchuk.autotrade.service.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;

    @Override
    public String login(String email, String password) {

        User user = userRepository
                .findByEmailIgnoreCase(email)
                .map(userMapper::toUser)
                .orElseThrow(() -> new UserNotFoundException("User with " + email + " not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Password is incorrect for {}", email);
            throw new PasswordIncorrectException("Password was incorrect for user with email " + email);
        }

        if (user.getStatus() == Status.Status.BLOCKED) {
            log.warn("User with email {} is blocked", email);
            throw new UserIsBlockedException("User is blocked");
        }

        return authTokenService.createToken(user);
    }
}
