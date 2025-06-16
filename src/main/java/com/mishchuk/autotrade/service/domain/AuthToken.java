package com.mishchuk.autotrade.service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "?????????")
public record AuthToken (
    String accessToken,
    String refreshToken,
    String tokenType,
    long expiresIn
) {
         public static final String BEARER = "Bearer";
    }
