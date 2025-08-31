package com.mishchuk.autotrade.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmailChangeDto {

    @NotBlank(message = "Old email must not be blank")
    @Email(message = "Invalid old email")
    private final String oldEmail;

    @NotBlank(message = "New email must not be blank")
    @Email(message = "Invalid new email")
    private final String newEmail;
}
