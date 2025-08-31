package com.mishchuk.autotrade.service.verify;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwilioVerifyService implements VerifyService {

    @Value("${sms.verify.twilio.account-sid}")
    private String accountSid;

    @Value("${sms.verify.twilio.auth-token}")
    private String authToken;

    @Value("${sms.verify.twilio.service-sid}")
    private String serviceSid;

    @PostConstruct
    void init() {
        Twilio.init(accountSid, authToken);
        log.info("✅ Twilio client initialized with Account SID {}", accountSid);
    }

    @Override
    public void start(String destination, Channel channel) {
        try {
            Verification verification = Verification.creator(
                    serviceSid,
                    destination,
                    channel.name().toLowerCase()
            ).create();
            log.info("Started {} verification for {} (status={})",
                    channel, destination, verification.getStatus());
        } catch (ApiException e) {
            log.error("❌ Failed to start {} verification for {}: {}",
                    channel, destination, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean check(String destination, String code) {
        try {
            VerificationCheck result = VerificationCheck.creator(serviceSid)
                    .setTo(destination)
                    .setCode(code)
                    .create();
            boolean approved = "approved".equalsIgnoreCase(result.getStatus());
            log.info("Verification check for {}: status={}, approved={}",
                    destination, result.getStatus(), approved);
            return approved;
        } catch (ApiException e) {
            log.error("❌ Verification check failed for {}: {}",
                    destination, e.getMessage(), e);
            return false;
        }
    }
}
