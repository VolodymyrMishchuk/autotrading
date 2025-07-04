package com.mishchuk.autotrade.service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

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
    private String id;
    private String firstName;
    private String lastName;
    private Instant birthDate;
    private String phoneNumber;
    private String email;
    private String password;
    private UserRole role;
    private Status status;
    protected Instant createdAt;
    protected Instant updatedAt;
    private UUID token;
}