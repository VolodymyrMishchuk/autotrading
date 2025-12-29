package com.mishchuk.autotrade.kafka;

import com.mishchuk.autotrade.service.signal.RawSignalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RawSignalListener {

    private final RawSignalService rawSignalService;

    @KafkaListener(topics = "signals.raw", groupId = "autotrade-signals")
    @Transactional
    public void consume(ConsumerRecord<String, String> record) {
        try {
            String value = record.value();
            log.info("💬 Kafka msg topic={} p={} off={} value={}",
                    record.topic(), record.partition(), record.offset(), value);
            rawSignalService.processIncomingRawMessage(
                    value, record.topic(), record.partition(), record.offset()
            );
        } catch (Exception e) {
            log.error("❌ Error processing Kafka message", e);
            // при потребі — кинути RuntimeException, щоб спрацювали ретраї/ДЛТ
        }
    }
}
