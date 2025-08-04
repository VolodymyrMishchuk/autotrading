package com.mishchuk.autotrade.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDate;

import lombok.*;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserCreateDto {
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDate;
    private final String phoneNumber;
    private final String email;
    private final String password;
}
