package com.mishchuk.autotrade.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, jakarta.servlet.http.HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        MDC.put("reqId", UUID.randomUUID().toString());
        MDC.put("ip", req.getRemoteAddr());
        MDC.put("ua", req.getHeader("User-Agent"));
        try { chain.doFilter(req, res); } finally { MDC.clear(); }
    }

    @Bean
    public FilterRegistrationBean<RequestContextFilter> register(RequestContextFilter f) {
        var bean = new FilterRegistrationBean<>(f);
        bean.setOrder(0);
        return bean;
    }
}