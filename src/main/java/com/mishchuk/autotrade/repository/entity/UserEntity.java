package com.mishchuk.autotrade.repository.entity;

import com.mishchuk.autotrade.service.model.Role;
import com.mishchuk.autotrade.service.model.Status;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_day")
    private Instant birthDay;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private Role role;

    @Column
    private Status status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}