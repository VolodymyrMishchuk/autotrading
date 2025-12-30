package com.mishchuk.autotrade.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.Year;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Override
    public void sendVerificationEmailHtml(
            String to,
            String subject,
            String userName,
            String activationLink,
            long expirationMinutes
    ) {
        // 1. Готуємо контекст для Thymeleaf
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("activationLink", activationLink);
        context.setVariable("expirationMinutes", expirationMinutes);
        context.setVariable("companyName", "AutoTrading");
        context.setVariable("year", Year.now().getValue());

        // 2. Генеруємо HTML з шаблону
        String htmlContent = templateEngine.process("email/verify", context);

        // 3. Plain-text fallback (на випадок старих клієнтів)
        String textFallback = String.format(
                """
                Вітаємо, %s!
                
                Щоб активувати акаунт, відкрийте посилання:
                %s
                
                Посилання дійсне %d хвилин.
                """,
                userName,
                activationLink,
                expirationMinutes
        );

        // 4. Відправка MimeMessage (HTML + text)
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    true,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(textFallback, htmlContent);

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification HTML email to " + to, e);
        }
    }
}
