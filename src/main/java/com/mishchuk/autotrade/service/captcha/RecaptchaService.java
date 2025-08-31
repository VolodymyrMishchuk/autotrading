package com.mishchuk.autotrade.service.captcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class RecaptchaService implements CaptchaService {

    @Value("${captcha.recaptcha.secret:}")
    private String secret;

    private final RestTemplate rt = new RestTemplate();

    @Override
    public boolean verify(String token, String remoteIp) {
        if (secret == null || secret.isBlank()) return true;
        String url = "https://www.google.com/recaptcha/api/siteverify";
        var params = new org.springframework.util.LinkedMultiValueMap<String, String>();
        params.add("secret", secret);
        params.add("response", token);
        if (remoteIp != null) params.add("remoteip", remoteIp);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<Map> resp = rt.postForEntity(url, new HttpEntity<>(params, headers), Map.class);
        Object ok = resp.getBody() != null ? resp.getBody().get("success") : null;
        return Boolean.TRUE.equals(ok);
    }
}