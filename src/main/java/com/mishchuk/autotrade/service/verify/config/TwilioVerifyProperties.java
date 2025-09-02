package com.mishchuk.autotrade.service.verify.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Validated
@ConfigurationProperties(prefix = "sms.verify.twilio")
public class TwilioVerifyProperties {
    @NotBlank private String accountSid;
    @NotBlank private String authToken;
    @NotBlank private String serviceSid;
}
