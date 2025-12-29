package com.mishchuk.autotrade.service.signal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mishchuk.autotrade.ingest.model.KafkaSignalDto;
import com.mishchuk.autotrade.repository.RawSignalRepository;
import com.mishchuk.autotrade.repository.entity.RawSignalEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class RawSignalService {

    private final RawSignalRepository rawSignalRepository;
    private final ObjectMapper objectMapper;

    /**
     * Основний шлях: приходить уже десеріалізований DTO із KafkaListener.
     */
    @Transactional
    public void processIncomingRawMessage(KafkaSignalDto dto,
                                          @Nullable String rawJson,
                                          String topic,
                                          int partition,
                                          long offset) {

        if (dto == null) {
            // Фолбек – зберегти як чистий raw JSON без метаданих (якщо є)
            RawSignalEntity entity = RawSignalEntity.builder()
                    .rawMessage(rawJson)
                    .rawJson(rawJson)
                    .kafkaTopic(topic)
                    .kafkaPartition(partition)
                    .kafkaOffset(offset)
                    .isEdit(false)
                    .version(1)
                    .receivedAt(Instant.now())
                    .build();

            rawSignalRepository.save(entity);
            log.info("💾 saved RAW(no-dto) id={} topic={} p={} off={}",
                    entity.getId(), topic, partition, offset);
            return;
        }

        final boolean isEdit = dto.editDate() != null;

        // Витягнути ідентифікатори
        final Long chatId = dto.chatId();
        final Integer messageId = dto.messageId();
        final Long senderId = dto.senderId();

        // Обчислюємо версію: перше повідомлення — v1, кожне редагування — +1
        int nextVersion = 1;
        if (chatId != null && messageId != null) {
            Integer maxVersion = rawSignalRepository.findMaxVersion(chatId, Long.valueOf(messageId));
            if (maxVersion == null) {
                nextVersion = 1;                 // перше входження
            } else {
                nextVersion = isEdit ? maxVersion + 1 : maxVersion; // не підвищуємо версію на дубль "не-редаг"
            }
        }

        RawSignalEntity entity = RawSignalEntity.builder()
                .rawMessage(rawOrEmpty(rawJson, dto))
                .rawJson(rawOrEmpty(rawJson, dto))
                .kafkaTopic(topic)
                .kafkaPartition(partition)
                .kafkaOffset(offset)

                .chatId(chatId)
                .chatTitle(dto.chatTitle())
                .messageId(messageId != null ? messageId.longValue() : null) // якщо в ентіті Long
                .senderId(senderId)
                .text(dto.text())
                .msgDate(dto.date())
                .editDate(dto.editDate())

                .isEdit(isEdit)
                .version(nextVersion)
                .receivedAt(Instant.now())
                .build();

        rawSignalRepository.save(entity);

        log.info("💾 saved raw signal id={} chat={} msg={} v={} edit={} topic={} p={} off={}",
                entity.getId(), entity.getChatId(), entity.getMessageId(),
                entity.getVersion(), entity.isEdit(), topic, partition, offset);
    }

    /**
     * Зворотно-сумісний шлях: коли слухач ще віддає String.
     * Ми пробуємо розпарсити DTO та делегуємо в основний метод.
     */
    @Transactional
    public void processIncomingRawMessage(String messageJson,
                                          String topic,
                                          Integer partition,
                                          Long offset) {
        KafkaSignalDto dto = null;
        try {
            dto = objectMapper.readValue(messageJson, KafkaSignalDto.class);
        } catch (Exception ex) {
            log.debug("RawSignalService: cannot parse KafkaSignalDto from JSON, will store raw only. Error: {}", ex.toString());
        }
        // Делегуємо в основний метод (partition/offset можуть бути null → підставимо дефолти)
        processIncomingRawMessage(
                dto,
                messageJson,
                topic,
                partition != null ? partition : -1,
                offset != null ? offset : -1L
        );
    }

    private String rawOrEmpty(@Nullable String rawJson, KafkaSignalDto dto) {
        if (rawJson != null && !rawJson.isBlank()) return rawJson;
        // якщо раптом нас викликали без rawJson — зберемо найпростіший JSON тільки з текстом
        // (щоб не втратити видимість оригіналу)
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (Exception e) {
            return "{\"text\":" + quote(dto.text()) + "}";
        }
    }

    private String quote(@Nullable String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
