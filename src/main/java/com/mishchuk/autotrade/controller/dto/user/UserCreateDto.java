package com.mishchuk.autotrade.controller.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserCreateDto {

    @NotBlank(message = "First name must not be blank")
    private final String firstName;

    private final String lastName;

    @NotBlank(message = "Birth date must not be blank")
    private final LocalDate birthDate;

    @NotBlank(message = "Phone number must not be blank")
    private final String phoneNumber;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,20}$",
            message = "Email має містити коректний домен (наприклад .com, .org, .ua)"
    )
    private final String email;

    @NotBlank(message = "Password must not be blank")
    private final String password;
}
