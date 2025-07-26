package com.mishchuk.autotrade.service.model;

import com.mishchuk.autotrade.enums.Direction;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Builder
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

    private BigDecimal amount;
    private Direction direction;
    private String description;
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @JoinColumn(name = "source_id")
    private String source;
}

