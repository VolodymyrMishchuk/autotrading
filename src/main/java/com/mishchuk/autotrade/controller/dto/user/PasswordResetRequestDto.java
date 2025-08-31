package com.mishchuk.autotrade.controller.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PasswordResetRequestDto {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    private final String email;
}
