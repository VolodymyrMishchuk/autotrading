package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.model.User;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class UserMapper {

    public User toUser(UserEntity entity) {
        return User.builder()
                .id(entity.getId() != null ? entity.getId().toString() : null)
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .birthDate(entity.getBirthDay() != null
                        ? entity.getBirthDay().atStartOfDay().toInstant(java.time.ZoneOffset.UTC)
                        : null)
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserEntity toUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId() != null ? UUID.fromString(user.getId()) : null)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDay(user.getBirthDate() != null
                        ? user.getBirthDate().atZone(java.time.ZoneOffset.UTC).toLocalDate()
                        : null)
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public User toUser(UserCreateDto dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    public UserDetailDto toUserDetailDto(User user) {
        return UserDetailDto.builder()
                .id(UUID.fromString(user.getId()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public User toUser(UserUpdateDto dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .build();
    }

    public User toUser(UserCompleteRegistrationDto dto) {
        return User.builder()
                .token(UUID.fromString(dto.getConfirmationToken()))
                .build();
    }
}