package com.mishchuk.autotrade.repository.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String metaTradeToken;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @ManyToMany
    @JoinTable(
            name = "cabinet_sources",
            joinColumns = @JoinColumn(name = "cabinet_id"),
            inverseJoinColumns = @JoinColumn(name = "source_id")
    )
    private List<SourceEntity> sources;

    @OneToMany(mappedBy = "cabinet", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions;
}