package com.mishchuk.autotrade.service.model;

import com.mishchuk.autotrade.enums.Status;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cabinet {

    private UUID id;
    private String name;
    private String metaTradeToken;
    private Status status;

    private UUID userId;
    private UUID accountId;
    private List<UUID> sourceIds;

    private Instant createdAt;
    private Instant updatedAt;
}