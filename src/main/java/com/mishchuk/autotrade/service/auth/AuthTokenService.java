package com.mishchuk.autotrade.service.auth;

import com.mishchuk.autotrade.service.model.User;
import com.mishchuk.autotrade.enums.UserRole;

public interface AuthTokenService {
    String createAccessToken(User user);
    String createRefreshToken();
    boolean isValidAccessToken(String token);
    String getUserId(String token);
    UserRole getUserRole(String token);
}
