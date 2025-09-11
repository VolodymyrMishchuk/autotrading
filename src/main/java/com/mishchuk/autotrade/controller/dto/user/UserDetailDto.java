package com.mishchuk.autotrade.controller.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDetailDto {
    private final UUID id;

    @NotBlank(message = "First name must not be blank")
    private final String firstName;

    private final String lastName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,20}$",
            message = "Email має містити коректний домен (наприклад .com, .org, .ua)"
    )
    private final String email;

    @NotBlank(message = "Phone number must not be blank")
    private final String phoneNumber;

    @NotBlank(message = "Birth date must not be blank")
    private final LocalDate birthDate;

    @NotBlank(message = "User role must not be blank")
    private final UserRole role;

    @NotBlank(message = "Status must not be blank")
    private final Status status;

    private final Instant createdAt;
    private final Instant updatedAt;
}
