package com.mishchuk.autotrade.controller.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResendVerificationRequestDto {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,20}$",
            message = "Email має містити коректний домен (наприклад .com, .org, .ua)"
    )
    private final String email;
}
