package com.mishchuk.autotrade.service.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue
    private String id;

    @JoinColumn(name = "account_id")
    private String account;
    private BigDecimal amount;
    private Direction direction;
    private String description;
    protected Instant createdAt;
    protected Instant updatedAt;
}

