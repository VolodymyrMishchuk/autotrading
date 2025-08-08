package com.mishchuk.autotrade.service.domain;

import com.mishchuk.autotrade.controller.dto.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.*;

@Builder
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
    private String firstName;
    private String lastName;

    @CreationTimestamp
    @Column(updatable = false)
    protected Instant birthDate;
    private String phoneNumber;
    private String email;
    private String password;
    private Role role;
    private Status status;
    private String token;

    @CreationTimestamp
    @Column(updatable = false)
    protected Instant createdAt;

    @UpdateTimestamp
    protected Instant updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();
}