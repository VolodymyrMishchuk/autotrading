package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.user.UserCompleteRegistrationDto;
import com.mishchuk.autotrade.controller.dto.user.UserCreateDto;
import com.mishchuk.autotrade.controller.dto.user.UserDetailDto;
import com.mishchuk.autotrade.controller.dto.user.UserUpdateDto;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserEntity entity) {
        if (entity == null) return null;

        return User.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .birthDate(entity.getBirthDate())
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .role(entity.getRole())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserEntity toUserEntity(User user) {
        if (user == null) return null;

        return UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public UserDetailDto toUserDetailDto(User user) {
        if (user == null) return null;

        return UserDetailDto.builder()
                .id(user.getId())
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
        if (dto == null) return null;

        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .build();
    }

    public User toUser(UserCompleteRegistrationDto dto) {
        if (dto == null) return null;

        return User.builder()
                .email(dto.getEmail())
                .build();
    }

    public UserEntity toUserEntity(UserCreateDto dto) {
        if (dto == null) return null;

        return UserEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}