package com.mishchuk.autotrade.service;

import com.mishchuk.autotrade.controller.dto.AuthLoginDto;
import com.mishchuk.autotrade.controller.dto.AuthTokenResponseDto;

public interface AuthService {
    AuthTokenResponseDto login(AuthLoginDto request);
}
