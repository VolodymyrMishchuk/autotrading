package com.mishchuk.autotrade.config;

import com.mishchuk.autotrade.enums.UserRole;
import com.mishchuk.autotrade.service.auth.AuthTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {

    private static final String BEARER_TOKEN_PREFIX = "Bearer ";
    private final AuthTokenService authTokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String path = request.getRequestURI();
        final String method = request.getMethod();

        log.info("🔐 Incoming request: {} {}", method, path);

        if (isPublic(method, path)) {
            log.info("✅ Public route — skip auth: {} {}", method, path);
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = getJwtFromRequest(request);
        if (!StringUtils.hasText(jwt) || !authTokenService.isValidAccessToken(jwt)) {
            log.warn("⛔ Invalid or missing token — 401 for {} {}", method, path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final String userId = authTokenService.getUserId(jwt);
        final UserRole userRole = authTokenService.getUserRole(jwt);

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : UserRole.values()) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
            if (role.equals(userRole)) break;
        }

        UserDetails userDetails = new User(userId, jwt, authorities);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, jwt, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        new RequestAttributeSecurityContextRepository().saveContext(context, request, response);

        log.info("🔓 User {} authenticated", userId);
        filterChain.doFilter(request, response);
    }

    // ---- helpers ----

    private boolean isPublic(String method, String path) {
        // preflight
        if ("OPTIONS".equalsIgnoreCase(method)) return true;

        // auth flow
        if ("POST".equals(method) && "/auth/signup".equals(path)) return true;
        if ("POST".equals(method) && "/auth/login".equals(path)) return true;

        // email verification links (GET з листа) + можливий POST із фронта
        if (("/auth/confirm".equals(path) || "/auth/signup/confirm".equals(path))) {
            return "GET".equals(method) || "POST".equals(method);
        }

        // ресенд підтвердження
        if ("POST".equals(method) && "/auth/resend-verification".equals(path)) return true;

        // якщо маєш інші публічні сторінки — додай тут

        return false;
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_TOKEN_PREFIX)) {
            return bearer.substring(BEARER_TOKEN_PREFIX.length());
        }
        return null;
    }
}
