package com.mishchuk.autotrade.repository.entity;

import com.mishchuk.autotrade.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "cabinet_sources")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CabinetSourceEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cabinet_id", nullable = false)
    private CabinetEntity cabinet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private SourceEntity source;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}