package com.mishchuk.autotrade.service.login;

import com.mishchuk.autotrade.controller.dto.auth.AuthTokenResponseDto;
public interface LoginService {
    AuthTokenResponseDto login(String email, String password);
}
