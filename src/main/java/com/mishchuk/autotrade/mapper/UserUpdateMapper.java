package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.service.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserUpdateMapper {

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

    public User toUser(PasswordChangeDto dto) {
        return User.builder()
                .password(dto.getNewPassword())
                .build();
    }

    public User toUser(PasswordResetRequestDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .build();
    }

    public User toUser(PasswordResetCompleteDto dto) {
        return User.builder()
                .token(UUID.fromString(dto.getToken()))
                .password(dto.getPassword())
                .build();
    }

    public User toUser(PhoneNumberChangeDto dto) {
        return User.builder()
                .phoneNumber(dto.getNewPhoneNumber())
                .build();
    }

    public User toUser(PhoneNumberResetRequestDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .build();
    }

    public User toUser(PhoneNumberResetCompleteDto dto) {
        return User.builder()
                .token(UUID.fromString(dto.getToken()))
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    public User toUser(RoleChangeDto dto) {
        return User.builder()
                .role(dto.getNewRole())
                .build();
    }
}