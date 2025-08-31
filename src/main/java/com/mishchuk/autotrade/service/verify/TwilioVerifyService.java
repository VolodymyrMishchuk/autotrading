package com.mishchuk.autotrade.service.verify;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwilioVerifyService implements VerifyService {

    @Value("${sms.verify.twilio.account-sid}")
    private String accountSid;

    @Value("${sms.verify.twilio.auth-token}")
    private String authToken;

    @Value("${sms.verify.twilio.service-sid}")
    private String serviceSid;

    @PostConstruct void init(){ Twilio.init(accountSid, authToken); }

    @Override public void start(String dest, Channel ch) {
        Verification.creator(serviceSid, dest, ch.name().toLowerCase()).create();
    }

    @Override public boolean check(String dest, String code) {
        try {
            var r = VerificationCheck.creator(serviceSid)
                    .setTo(dest).setCode(code).create();
            return "approved".equalsIgnoreCase(r.getStatus());
        } catch (Exception e) { return false; }
    }
}
