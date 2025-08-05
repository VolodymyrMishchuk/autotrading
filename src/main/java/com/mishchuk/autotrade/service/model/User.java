package com.mishchuk.autotrade.service.model;

import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.enums.UserRole;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String password;
    private UserRole role;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID token;
}