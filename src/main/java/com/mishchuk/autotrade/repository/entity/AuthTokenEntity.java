package com.mishchuk.autotrade.repository.entity;

import com.mishchuk.autotrade.service.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_tokens")
public class AuthTokenEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String refreshToken;

    @Column
    private Instant expiryDate;
}
