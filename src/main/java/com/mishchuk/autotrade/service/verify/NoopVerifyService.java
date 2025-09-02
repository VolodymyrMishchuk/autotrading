package com.mishchuk.autotrade.service.verify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "sms.verify", name = "provider", havingValue = "noop", matchIfMissing = true)
public class NoopVerifyService implements VerifyService {

    @Override
    public void start(String destination, Channel channel) {
        log.debug("[NOOP] start {} verification for {}", channel, destination);
    }

    @Override
    public boolean check(String destination, String code) {
        log.debug("[NOOP] check verification for {}, code={}", destination, code);
        return true;
    }
}
