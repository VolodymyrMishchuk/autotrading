package com.mishchuk.autotrade.service.user;

import com.mishchuk.autotrade.service.model.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    void createUser(User user);
    User getUserById(UUID id);
    User getUserByToken(String token);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(UUID id);
    void completeRegistration(String token);
    User getAuthenticatedUser();
    void updatePhoneNumberOfUser(User user);
    void updateEmailOfUser(User user);
    void updatePasswordOfUser(User user);
    void updateRoleOfUser(User user);
    void updateStatusOfUser(User user);
}