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
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    private String id;

    @JoinColumn(name = "user_id")
    private String user;
    private String name;
    private String tokenMetaApi;
    private Status status;
    private BigDecimal balance;
    protected Instant createdAt;
    protected Instant updatedAt;
}
