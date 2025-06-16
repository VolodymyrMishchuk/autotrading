package com.mishchuk.autotrade.service.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean emailConfirmed = false;

    @CreationTimestamp
    @Column(updatable = false)
    protected Instant createdAt;

    @UpdateTimestamp
    protected Instant updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();
}