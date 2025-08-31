package com.mishchuk.autotrade.service.user;

import com.mishchuk.autotrade.service.model.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(UUID id);

    void updateRoleOfUser(User user);
    void updateStatusOfUser(User user);

    void changePassword(UUID userId, String oldPassword, String newPassword);
    void requestPasswordReset(String email);
    void completePasswordReset(String token, String newPassword);

    void changePhoneNumber(UUID userId, String oldPhone, String newPhone);
    void requestPhoneNumberReset(String email);
    void completePhoneNumberReset(String token, String newPhone);

    void changeEmail(UUID userId, String oldEmail, String newEmail);
    void requestEmailResetByPhone(String phoneNumber);
    void completeEmailResetBySms(String verificationCodeBySMS, String newEmail);
}