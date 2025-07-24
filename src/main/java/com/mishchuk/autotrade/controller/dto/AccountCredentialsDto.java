package com.mishchuk.autotrade.controller.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class AccountCredentialsDto {
    private final UUID accountId;
    private final String token;
}
