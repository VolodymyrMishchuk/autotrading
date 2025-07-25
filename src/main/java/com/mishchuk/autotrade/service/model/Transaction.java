package com.mishchuk.autotrade.service.model;

import com.mishchuk.autotrade.controller.dto.Direction;
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
    protected Instant createdAt;

    @JoinColumn(name = "account_id")
    private String account;

    @JoinColumn(name = "source_id")
    private String source;
}

