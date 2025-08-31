package com.mishchuk.autotrade.service.captcha;
public interface CaptchaService {
    boolean verify(String token, String remoteIp);
}