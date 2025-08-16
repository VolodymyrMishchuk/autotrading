package com.mishchuk.autotrade.repository.entity;

import com.mishchuk.autotrade.enums.Status;
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
@Table(name = "td_session")
public class TdSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "api_id", nullable = false)
    private Integer apiId; // Telegram app id (int)

    @Column(name = "api_hash", nullable = false)
    private String apiHash; // зберігай безпечно (secret manager/шифрування)

    @Column(name = "db_dir", nullable = false, unique = true)
    private String dbDir; // каталог TDLib для цієї сесії

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // ACTIVE / INACTIVE

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
