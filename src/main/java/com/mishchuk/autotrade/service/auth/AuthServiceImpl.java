package com.mishchuk.autotrade.service.auth;

import com.mishchuk.autotrade.controller.dto.AuthLoginDto;
import com.mishchuk.autotrade.controller.dto.AuthTokenResponseDto;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Override
    public AuthTokenResponseDto login(AuthLoginDto request) {
        // TODO: validate credentials, generate JWT token
        return new AuthTokenResponseDto("dummy-token", "bearer");
    }
}
