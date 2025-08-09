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

    private static final List<String> PUBLIC_PATH_PREFIXES = List.of(
            "/auth/signup",
            "/auth/login",
            "/auth/signup/confirm",
            "/auth/resend-verification",
            "/auth/confirm"
    );

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        log.info("🔐 Incoming request: {} {}", method, path);

        // 🟢 Пропускаємо preflight OPTIONS
        if ("OPTIONS".equalsIgnoreCase(method)) {
            log.info("🟢 OPTIONS preflight — skipping");
            filterChain.doFilter(request, response);
            return;
        }

        // 🟢 Пропускаємо публічні маршрути
        if (isPublicPath(path)) {
            log.info("✅ Public path detected — skipping");
            filterChain.doFilter(request, response);
            return;
        }

        // 🔒 Перевіряємо JWT
        String jwt = getJwtFromRequest(request);
        if (!StringUtils.hasText(jwt) || !authTokenService.isValidAccessToken(jwt)) {
            log.warn("⛔ Invalid or missing token — returning 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // ✅ Створення контексту безпеки
        String userId = authTokenService.getUserId(jwt);
        UserRole userRole = authTokenService.getUserRole(jwt);

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

        RequestAttributeSecurityContextRepository contextRepo = new RequestAttributeSecurityContextRepository();
        contextRepo.saveContext(context, request, response);

        log.info("🔓 User {} authenticated", userId);
        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        log.info("🔍 Checking if '{}' is public", path);

        for (String publicPath : PUBLIC_PATH_PREFIXES) {
            if (path.startsWith(publicPath)) {
                log.info("✅ Allowed public path matched: {}", publicPath);
                return true;
            }
        }

        log.warn("⛔ Path '{}' is not public", path);
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