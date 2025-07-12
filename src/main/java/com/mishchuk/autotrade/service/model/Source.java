package com.mishchuk.autotrade.service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sources")
public class Source {
    @Id
    @GeneratedValue
    private String id;
    private String name;
    private String platform;
    private String token;
    private Status status;
    protected Instant createdAt;
    protected Instant updatedAt;
}
