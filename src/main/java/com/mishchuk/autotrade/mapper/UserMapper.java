package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.service.model.User;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {

    public User toUser(UserEntity entity) {
        return User.builder()
                .id(entity.getId() != null ? entity.getId().toString() : null)
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .birthDate(entity.getBirthDay())
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .status(entity.getStatus()) //  а тут тре пассворд?
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .token(entity.getToken()) // чи тре тут токен?
                .build();
    }

    public UserEntity toUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId() != null ? UUID.fromString(user.getId()) : null)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDay(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .password(user.getPassword()) // знову ж таки питання, чи доречне тут це поле?
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .token(user.getToken()) // ???
                .build();
    }

    public User toUser(UserRegistrationDto dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .password(dto.getPassword())
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

    public User toUser(EmailChangeDto dto) {
        return User.builder()
                .email(dto.getNewEmail())
                .build();
    }

    public User toUser(EmailResetRequestDto dto) {
        return User.builder()
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    public User toUser(EmailResetCompleteDto dto) {
        return User.builder()
                .token(UUID.fromString(dto.getToken()))
                .email(dto.getEmail())
                .build();
    }

    public User toDomain(PasswordChangeDto dto) {
        return User.builder()
                .password(dto.getNewPassword())
                .build();
    }

    public User toDomain(PasswordResetRequestDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .build();
    }

    public User toDomain(PasswordResetCompleteDto dto) {
        return User.builder()
                .token(UUID.fromString(dto.getToken()))
                .password(dto.getPassword())
                .build();
    }

    public User toDomain(PhoneNumberChangeDto dto) {
        return User.builder()
                .phoneNumber(dto.getNewPhoneNumber())
                .build();
    }

    public User toDomain(PhoneNumberResetRequestDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .build();
    }

    public User toDomain(PhoneNumberResetCompleteDto dto) {
        return User.builder()
                .token(UUID.fromString(dto.getToken()))
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    public User toDomain(RoleChangeDto dto) {
        return User.builder()
                .role(dto.getNewRole())
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
}
