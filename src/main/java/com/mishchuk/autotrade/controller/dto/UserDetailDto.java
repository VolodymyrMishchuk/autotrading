package com.mishchuk.autotrade.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.enums.UserRole;
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
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final LocalDate birthDate;
    private final UserRole role;
    private final Status status;
    private final Instant createdAt;
    private final Instant updatedAt;
}
