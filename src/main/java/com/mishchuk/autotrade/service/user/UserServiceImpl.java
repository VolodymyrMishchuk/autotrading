package com.mishchuk.autotrade.service.user;

import com.mishchuk.autotrade.service.model.User;
import com.mishchuk.autotrade.service.user.admin.UserAdminService;
import com.mishchuk.autotrade.service.user.contact.UserContactService;
import com.mishchuk.autotrade.service.user.credentials.UserCredentialsService;
import com.mishchuk.autotrade.service.user.profile.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserProfileService profile;
    private final UserAdminService admin;
    private final UserCredentialsService credentials;
    private final UserContactService contact;

    @Override
    public User getUserById(UUID id) { return profile.getUserById(id); }

    @Override
    public List<User> getAllUsers() { return profile.getAllUsers(); }

    @Override
    public void updateUser(User user) { profile.updateUser(user); }

    @Override
    public void deleteUser(UUID id) { profile.deleteUser(id); }

    @Override
    public void updateRoleOfUser(User user) { admin.updateRoleOfUser(user); }

    @Override
    public void updateStatusOfUser(User user) { admin.updateStatusOfUser(user); }

    @Override
    public void changePassword(UUID userId, String oldPassword, String newPassword) {
        credentials.changePassword(userId, oldPassword, newPassword);
    }

    @Override
    public void requestPasswordReset(String email) { credentials.requestPasswordReset(email); }

    @Override
    public void completePasswordReset(String token, String newPassword) {
        credentials.completePasswordReset(token, newPassword);
    }

    @Override
    public void changePhoneNumber(UUID userId, String oldPhone, String newPhone) {
        contact.changePhoneNumber(userId, oldPhone, newPhone);
    }

    @Override
    public void requestPhoneNumberReset(String email) { contact.requestPhoneNumberReset(email); }

    @Override
    public void completePhoneNumberReset(String token, String newPhone) {
        contact.completePhoneNumberReset(token, newPhone);
    }

    @Override public void changeEmail(UUID userId, String oldEmail, String newEmail) {
        contact.changeEmail(userId, oldEmail, newEmail);
    }

    @Override public void requestEmailResetByPhone(String phoneNumber) {
        contact.requestEmailResetByPhone(phoneNumber);
    }

    @Override public void completeEmailResetBySms(String verificationCodeBySMS, String newEmail) {
        contact.completeEmailResetBySms(verificationCodeBySMS, newEmail);
    }
}