package com.mishchuk.autotrade.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserUpdateDto {
    private final String firstName;
    private final String lastName;
    private final Instant birthDate;
    private final String phoneNumber;
    private final String email;
    private final String password;
    private final Role role;
    private final Status status;
}
