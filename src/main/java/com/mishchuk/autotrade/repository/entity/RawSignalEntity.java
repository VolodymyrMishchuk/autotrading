package com.mishchuk.autotrade.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "raw_signals")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RawSignalEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "raw_message", nullable = false, columnDefinition = "TEXT")
    private String rawMessage;

    @Column(name = "received_at", nullable = false)
    @Builder.Default
    private Instant receivedAt = Instant.now();

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "chat_title", columnDefinition = "TEXT")
    private String chatTitle;

    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "msg_date")
    private Instant msgDate;

    @Column(name = "edit_date")
    private Instant editDate;

    @Column(name = "is_edit", nullable = false)
    @Builder.Default
    private boolean isEdit = false;

    @Column(name = "version", nullable = false)
    @Builder.Default
    private Integer version = 1;

    @Column(name = "kafka_topic", length = 128)
    private String kafkaTopic;

    @Column(name = "kafka_partition")
    private Integer kafkaPartition;

    @Column(name = "kafka_offset")
    private Long kafkaOffset;

    @Column(name = "raw_json", columnDefinition = "jsonb")
    private String rawJson;
}
