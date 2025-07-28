package com.mishchuk.autotrade.repository.entity;

import com.mishchuk.autotrade.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cabinets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CabinetEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "meta_trade_token")
    private String metaTradeToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @OneToMany(mappedBy = "cabinet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CabinetSourceEntity> sources;

    @OneToMany(mappedBy = "cabinet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactions;
}