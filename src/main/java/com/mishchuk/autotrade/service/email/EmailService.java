package com.mishchuk.autotrade.service.email;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}