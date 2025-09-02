package com.mishchuk.autotrade.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestContextCaptureFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        MDC.put("reqId", UUID.randomUUID().toString());
        MDC.put("ip", req.getRemoteAddr());
        MDC.put("ua", req.getHeader("User-Agent"));
        try {
            chain.doFilter(req, res);
        } finally {
            MDC.clear();
        }
    }
}
