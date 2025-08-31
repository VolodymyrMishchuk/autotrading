package com.mishchuk.autotrade.service.captcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TurnstileService implements CaptchaService {

    @Value("${captcha.turnstile.secret:}")
    private String secret;
    private final RestTemplate rt = new RestTemplate();

    @Override
    public boolean verify(String token, String remoteIp) {
        if (secret == null || secret.isBlank()) return true;
        String url = "https://challenges.cloudflare.com/turnstile/v0/siteverify";
        var payload = Map.of("secret", secret, "response", token, "remoteip", remoteIp);
        ResponseEntity<Map> resp = rt.postForEntity(url, payload, Map.class);
        Object ok = resp.getBody() != null ? resp.getBody().get("success") : null;
        return Boolean.TRUE.equals(ok);
    }
}
