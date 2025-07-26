package com.mishchuk.autotrade.service.model;

import com.mishchuk.autotrade.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cabinets")
public class Cabinet {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String metaTradeToken;
    private Status status;
    private UUID userId;
    private UUID accountId;
    private List<UUID> sourceIds;
}