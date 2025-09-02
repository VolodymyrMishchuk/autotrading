package com.mishchuk.autotrade.service.verify.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "sms.verify", name = "provider", havingValue = "twilio")
@EnableConfigurationProperties(TwilioVerifyProperties.class)
public class TwilioVerifyAutoConfiguration {}
