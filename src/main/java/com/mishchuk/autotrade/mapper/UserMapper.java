package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.service.domain.User;
import com.mishchuk.autotrade.repository.entity.UserEntity;
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
                .role(Role.valueOf(user.getRole()))
                .status(Status.valueOf(user.getStatus()))
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
                .role(String.valueOf(dto.getRole()))
                .status(String.valueOf(dto.getStatus()))
                .password(dto.getPassword())
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
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .roles(entity.getRoles().stream().map(Role::valueOf).toList())
                .status(UserStatus.valueOf(entity.getStatus()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserEntity toUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId() != null ? UUID.fromString(user.getId()) : null)
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .status(user.getStatus().name())
                .roles(user.getRoles().stream().map(Enum::name).toList())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
