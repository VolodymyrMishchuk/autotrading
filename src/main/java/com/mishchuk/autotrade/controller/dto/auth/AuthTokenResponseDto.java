package com.mishchuk.autotrade.controller.dto.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthTokenResponseDto {
    private final String accessToken;
    private final String refreshToken;
    private final String tokenType = "bearer";
}
