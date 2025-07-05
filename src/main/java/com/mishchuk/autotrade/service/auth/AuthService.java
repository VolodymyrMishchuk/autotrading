package com.mishchuk.autotrade.service.auth;

import com.mishchuk.autotrade.service.model.User;
import com.mishchuk.autotrade.service.model.UserRole;
import jakarta.validation.constraints.Email;

public interface AuthService {
    String createToken(User user);
    boolean isValidToken(String token);
    String getUserId(String token);
    UserRole getUserRole(String token);
}
