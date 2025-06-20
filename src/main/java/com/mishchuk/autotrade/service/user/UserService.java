package com.mishchuk.autotrade.service.user;

import com.mishchuk.autotrade.controller.dto.UserCreateDto;
import com.mishchuk.autotrade.controller.dto.UserDetailDto;
import com.mishchuk.autotrade.controller.dto.UserUpdateDto;

import java.util.UUID;

public interface UserService {
    UserDetailDto createUser(UserCreateDto request);
    void completeRegistration(UserCompleteRegistrationDto request);
    UserDetailDto updateUser(UUID id, UserUpdateDto request);
    UserDetailDto getUser(UUID id);
    void deleteUser(UUID id);
}