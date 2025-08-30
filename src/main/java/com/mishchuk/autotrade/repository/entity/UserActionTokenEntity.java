package com.mishchuk.autotrade.repository.entity;

import com.mishchuk.autotrade.enums.TokenChannel;
import com.mishchuk.autotrade.enums.TokenPurpose;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_action_tokens",
        indexes = {
                @Index(name = "idx_uat_token", columnList = "token", unique = true),
                @Index(name = "idx_uat_user_purpose", columnList = "user_id,purpose")
        }
)
public class UserActionTokenEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", nullable = false)
    private TokenPurpose purpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private TokenChannel channel;

    @Column(name = "data", columnDefinition = "jsonb")
    private String data;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "consumed_at")
    private Instant consumedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
