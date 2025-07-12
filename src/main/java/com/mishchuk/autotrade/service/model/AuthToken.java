package com.mishchuk.autotrade.service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "?????????")
public class AuthToken {
    @Id
    @GeneratedValue
    private String id;

    @JoinColumn(name = "user_id")
    private String user;
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    protected Instant createdAt;
    protected Instant updatedAt;
}

