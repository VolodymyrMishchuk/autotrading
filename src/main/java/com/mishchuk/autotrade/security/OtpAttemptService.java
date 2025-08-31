package com.mishchuk.autotrade.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OtpAttemptService {
    private final Cache<String, AtomicInteger> attempts = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(15))
            .build();
    private final Cache<String, Boolean> locked = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5))
            .build();

    public boolean isLocked(String key) { return locked.getIfPresent(key) != null; }
    public void recordFailure(String key, int threshold) {
        AtomicInteger c = attempts.get(key, k -> new AtomicInteger(0));
        if (c.incrementAndGet() >= threshold) {
            locked.put(key, true); attempts.invalidate(key);
        }
    }
    public void reset(String key) { attempts.invalidate(key); locked.invalidate(key); }
}
