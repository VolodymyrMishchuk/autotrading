package com.mishchuk.autotrade.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Set<String> PROTECTED = Set.of(
            "/users/password/reset/request",
            "/users/phone/reset/request",
            "/users/email/reset/request",
            "/auth/login",
            "/auth/signup",
            "/auth/resend-verification"
    );

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private static final Bandwidth RULE =
            Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(15)));

    private Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, k -> Bucket.builder().addLimit(RULE).build());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        String path = request.getRequestURI();
        return PROTECTED.stream().noneMatch(path::equals);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String key = req.getRemoteAddr();
        Bucket bucket = resolveBucket(key);
        if (bucket.tryConsume(1)) {
            chain.doFilter(req, res);
        } else {
            String origin = req.getHeader("Origin");
            if (origin != null) {
                res.setHeader("Access-Control-Allow-Origin", origin);
                res.setHeader("Vary", "Origin");
                res.setHeader("Access-Control-Allow-Credentials", "true");
            }
            res.setStatus(429);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"rate_limited\"}");
        }
    }
}
