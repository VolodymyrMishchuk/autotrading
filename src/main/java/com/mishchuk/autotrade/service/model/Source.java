package com.mishchuk.autotrade.service.model;

import com.mishchuk.autotrade.enums.Status;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Source {
    private UUID id;
    private String name;
    private String platform;
    private String token;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;
}