package com.mishchuk.autotrade.service.user.contact;

import java.util.UUID;

public interface UserContactService {

    void changePhoneNumber(UUID userId, String oldPhone, String newPhone);
    void requestPhoneNumberReset(String email);
    void completePhoneNumberReset(String token, String newPhone);

    void changeEmail(UUID userId, String oldEmail, String newEmail);
    void requestEmailResetByPhone(String phoneNumber);
    void completeEmailResetBySms(String code, String newEmail);
}