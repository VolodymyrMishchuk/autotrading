package com.mishchuk.autotrade.service.registration;

import com.mishchuk.autotrade.controller.dto.UserCreateDto;

import java.util.UUID;

public interface RegistrationService {
    UUID register(UserCreateDto dto);
    boolean confirmByToken(String token);
    boolean confirmByEmailAndToken(String email, String token);
}