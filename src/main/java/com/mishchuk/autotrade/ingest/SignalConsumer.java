package com.mishchuk.autotrade.ingest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mishchuk.autotrade.controller.dto.transaction.TransactionCreateDto;
import com.mishchuk.autotrade.enums.Direction;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.repository.CabinetSourceRepository;
import com.mishchuk.autotrade.repository.SourceRepository;
import com.mishchuk.autotrade.repository.entity.SourceEntity;
import com.mishchuk.autotrade.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.mishchuk.autotrade.enums.Type;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignalConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SourceRepository sourceRepo;
    private final CabinetSourceRepository cabinetSourceRepo;
    private final TransactionService txService;

    @KafkaListener(topics = "${app_kafka.topic}", groupId = "autotrade-signals")
    public void onMessage(ConsumerRecord<String, String> record) throws Exception {
        String json = record.value();
        SignalMessage msg = objectMapper.readValue(json, SignalMessage.class);

        UUID sourceId = UUID.fromString(msg.getSourceId());
        Optional<SourceEntity> opt = sourceRepo.findById(sourceId);
        if (opt.isEmpty()) {
            log.warn("Signal for unknown source: {}", sourceId);
            return;
        }
        SourceEntity src = opt.get();
        if (src.getStatus() != Status.ACTIVE) {
            log.debug("Source {} not active, skip", sourceId);
            return;
        }

        Type type = msg.getType();
        if (type == null) {
            log.warn("Signal without type, payload={}", json);
            return;
        }

        switch (type) {
            case OPEN  -> handleOpen(src, msg);
            case CLOSE -> handleClose(src, msg);
            default    -> log.warn("Unknown signal type: {}", type);
        }
    }

    private void handleOpen(SourceEntity src, SignalMessage m) {
        Direction dir = m.getDirection();
        BigDecimal amount = m.getAmount() != null ? m.getAmount() : BigDecimal.ONE;

        cabinetSourceRepo.findAllBySourceId(src.getId()).forEach(cs -> {
            try {
                TransactionCreateDto dto = TransactionCreateDto.builder()
                        .symbol(m.getSymbol())
                        .amount(amount)
                        .direction(dir)
                        .openedAt(Instant.now())
                        .userId(cs.getCabinet().getUser().getId())
                        .accountId(src.getAccount().getId())
                        .cabinetId(cs.getCabinet().getId())
                        .sourceId(src.getId())
                        .build();
                txService.createTransaction(dto);
            } catch (Exception e) {
                log.error("OPEN error (source={}, cabinet={}): {}", src.getId(), cs.getCabinet().getId(), e.getMessage(), e);
            }
        });

        log.info("[KAFKA] Source {}: OPEN {} {}", src.getId(), m.getDirection(), m.getSymbol());
    }

    private void handleClose(SourceEntity src, SignalMessage m) {
        cabinetSourceRepo.findAllBySourceId(src.getId()).forEach(cs -> {
            try {
                TransactionCreateDto dto = TransactionCreateDto.builder()
                        .symbol(m.getSymbol())
                        .amount(BigDecimal.ZERO)
                        .direction(Direction.BUY)
                        .closedAt(Instant.now())
                        .userId(cs.getCabinet().getUser().getId())
                        .accountId(src.getAccount().getId())
                        .cabinetId(cs.getCabinet().getId())
                        .sourceId(src.getId())
                        .build();
                txService.createTransaction(dto);
            } catch (Exception e) {
                log.error("CLOSE error (source={}, cabinet={}): {}", src.getId(), cs.getCabinet().getId(), e.getMessage(), e);
            }
        });

        log.info("[KAFKA] Source {}: CLOSE {}", src.getId(), m.getSymbol());
    }
}
