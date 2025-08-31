package com.mishchuk.autotrade.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PhoneNumberChangeDto {

    @NotBlank(message = "Old phone number must not be blank")
    @Pattern(
            regexp = "^\\+?(?=(?:\\D*\\d){10,15}\\D*$)[\\d\\s()\\-]+$",
            message = "Invalid phone number format"
    )
    private final String oldPhoneNumber;

    @NotBlank(message = "New phone number must not be blank")
    @Pattern(
            regexp = "^\\+?(?=(?:\\D*\\d){10,15}\\D*$)[\\d\\s()\\-]+$",
            message = "Invalid phone number format"
    )
    private final String newPhoneNumber;
}
