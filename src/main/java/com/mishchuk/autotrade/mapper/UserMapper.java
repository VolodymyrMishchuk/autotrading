package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.service.domain.*;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.domain.Role;
import com.mishchuk.autotrade.controller.dto.Role;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {

    public UserDetailDto toUserDetailDto(User user) {
        return UserDetailDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    public User toUser(UserCreateDto dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .build();
    }

    public User toUser(UserUpdateDto dto) {
        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .status(dto.getStatus())
                .build();
    }

    public User toUser(UserCompleteRegistrationDto dto) {
        return User.builder()
                .token(dto.getToken())
                .password(dto.getPassword())
                .build();
    }

    public User toUser(UserEntity entity) {
        return User.builder()
                .id(UUID.fromString(entity.getId().toString()))
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .birthDate(entity.getBirthDay())
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .role(entity.getRole())
                .status(String.valueOf(entity.getStatus()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserEntity toUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId() != null ? user.getId() : null)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDay(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
