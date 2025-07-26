package com.mishchuk.autotrade.config;

import com.mishchuk.autotrade.enums.UserRole;
import com.mishchuk.autotrade.service.auth.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {

    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);

        if (!StringUtils.hasText(jwt) || !tokenService.isValidToken(jwt)) {
            // Skip authentication due to invalid JWT.
            filterChain.doFilter(request, response);
            return;
        }

        String id = tokenService.getUserId(jwt);
        UserRole userUpperBoundaryRole = tokenService.getUserRole(jwt);

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (UserRole userRole : UserRole.values()) {

            authorities.add(new SimpleGrantedAuthority(userRole.toString()));

            if (userRole.equals(userUpperBoundaryRole)) {
                break;
            }
        }

        UserDetails userDetails = new User(id, jwt, authorities);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, jwt, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext securityContext = SecurityContextHolder.getContext();

        securityContext.setAuthentication(authentication);

        RequestAttributeSecurityContextRepository requestAttributeSecurityContextRepository =
                new RequestAttributeSecurityContextRepository();

        requestAttributeSecurityContextRepository.saveContext(securityContext, request, response);

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TOKEN_PREFIX)) {
            return bearerToken.substring(BEARER_TOKEN_PREFIX.length());
        }
        return null;
    }
}