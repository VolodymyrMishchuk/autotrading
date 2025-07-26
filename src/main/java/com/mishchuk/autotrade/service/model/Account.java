package com.mishchuk.autotrade.service.model;

import com.mishchuk.autotrade.enums.Status;
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
    private String tokenMetaApi;
    private Status.Status status;
    private BigDecimal balance;
    private String currency;
    protected Instant createdAt;
    protected Instant updatedAt;

    @JoinColumn(name = "user_id")
    private String user;

    @ManyToMany(fetch = FetchType.LAZY)
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
