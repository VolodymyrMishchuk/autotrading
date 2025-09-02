package com.mishchuk.autotrade.service.verify;

import com.mishchuk.autotrade.service.verify.config.TwilioVerifyProperties;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "sms.verify", name = "provider", havingValue = "twilio")
public class TwilioVerifyService implements VerifyService {

    private final TwilioVerifyProperties props;

    @PostConstruct
    void init() {
        Twilio.init(props.getAccountSid(), props.getAuthToken());
        log.info("✅ Twilio Verify initialized (accountSid={})", mask(props.getAccountSid()));
    }

    @Override
    public void start(String destination, Channel channel) {
        try {
            Verification verification = Verification
                    .creator(props.getServiceSid(), destination, channel.name().toLowerCase())
                    .create();

            log.info("Started {} verification for {} (status={})",
                    channel, destination, verification.getStatus());
        } catch (ApiException e) {
            log.error("❌ Failed to start {} verification for {}: [{}] {}",
                    channel, destination, e.getCode(), e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean check(String destination, String code) {
        try {
            VerificationCheck result = VerificationCheck
                    .creator(props.getServiceSid())
                    .setTo(destination)
                    .setCode(code)
                    .create();

            boolean approved = "approved".equalsIgnoreCase(result.getStatus());
            log.info("Verification check for {}: status={}, approved={}",
                    destination, result.getStatus(), approved);
            return approved;
        } catch (ApiException e) {
            log.warn("❌ Verification check failed for {}: [{}] {}",
                    destination, e.getCode(), e.getMessage());
            return false;
        }
    }

    private static String mask(String sid) {
        if (sid == null || sid.length() < 6) return "****";
        return sid.substring(0, 2) + "****" + sid.substring(sid.length() - 4);
    }
}
