package com.mishchuk.autotrade.controller.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmailChangeDto {

    @NotBlank(message = "Old email must not be blank")
    @Email(message = "Invalid old email")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,20}$",
            message = "Email має містити коректний домен (наприклад .com, .org, .ua)")
    private final String oldEmail;

    @NotBlank(message = "New email must not be blank")
    @Email(message = "Invalid new email")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,20}$",
            message = "Email має містити коректний домен (наприклад .com, .org, .ua)")
    private final String newEmail;
}