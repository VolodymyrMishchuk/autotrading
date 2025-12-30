package com.mishchuk.autotrade.service.email;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendVerificationEmailHtml(String to, String subject, String userName, String activationLink, long expirationMinutes);
}