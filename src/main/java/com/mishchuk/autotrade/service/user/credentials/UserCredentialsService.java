package com.mishchuk.autotrade.service.user.credentials;

public interface UserCredentialsService {
    void changePassword(java.util.UUID userId, String oldPassword, String newPassword);
    void requestPasswordReset(String email);
    void completePasswordReset(String token, String newPassword);
}