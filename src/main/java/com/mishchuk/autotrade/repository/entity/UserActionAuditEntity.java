package com.mishchuk.autotrade.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_action_audit")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActionAuditEntity {
    @Id @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable=false)
    private String action;
    private String channel;
    private String target;
    private String ip;
    private String userAgent;

    @Column(nullable=false)
    private boolean success;

    @Column(nullable=false)
    private Instant createdAt;
}