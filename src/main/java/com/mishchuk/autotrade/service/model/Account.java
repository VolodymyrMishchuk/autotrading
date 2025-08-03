package com.mishchuk.autotrade.service.model;

import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    private String id;

    private String name;

    @Column(name = "token_MetaTradeAPI", nullable = false)
    private String tokenMetaTradeAPI;
    private Status status;
    private BigDecimal balance;

    @Column(name = "created_at", nullable = false)
    protected Instant createdAt;

    @Column(name = "updated_at")
    protected Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @JoinTable(
            name = "account_sources",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "source_id")
    )
    private Set<Source> sources = new HashSet<>();

    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Transaction> transactions = new ArrayList<>();
}
