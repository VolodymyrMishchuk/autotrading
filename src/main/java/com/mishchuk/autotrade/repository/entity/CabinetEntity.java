package com.mishchuk.autotrade.repository.entity;

import com.mishchuk.autotrade.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cabinets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CabinetEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "meta_trade_token", nullable = false)
    private String metaTradeToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "cabinet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CabinetSourceEntity> sources = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "cabinet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactions = new ArrayList<>();
}