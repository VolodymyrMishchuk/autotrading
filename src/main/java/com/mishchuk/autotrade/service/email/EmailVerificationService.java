package com.mishchuk.autotrade.service.email;

import com.mishchuk.autotrade.repository.entity.UserEntity;

public interface EmailVerificationService {
    void sendVerificationEmail(UserEntity user);
    boolean confirmToken(String token);
    void resendVerificationEmail(UserEntity user);
}