package com.mishchuk.autotrade.service.model;

import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.repository.entity.AccountEntity;
import com.mishchuk.autotrade.repository.entity.SourceEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cabinets")
public class Cabinet {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(name = "meta_trade_token")
    private String metaTradeToken;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @ManyToMany
    @JoinTable(
            name = "cabinet_sources",
            joinColumns = @JoinColumn(name = "cabinet_id"),
            inverseJoinColumns = @JoinColumn(name = "source_id")
    )
    private List<SourceEntity> sources = new ArrayList<>();
}