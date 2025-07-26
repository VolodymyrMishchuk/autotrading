package com.mishchuk.autotrade.service.auth;

import com.mishchuk.autotrade.service.model.User;
import com.mishchuk.autotrade.enums.UserRole;

public interface TokenService {
    String createToken(User user);
    boolean isValidToken(String token);
    String getUserId(String token);
    UserRole getUserRole(String token);
}
