package com.mishchuk.autotrade.service.user;

import com.mishchuk.autotrade.service.model.User;
import java.util.List;

public interface UserService {
    void createUser(User user);
    User getUserById(String id);
    User getUserByToken(String token);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(String id);
    void completeRegistration(String token);
    User getAuthenticatedUser();
    void updateEmailOfUser(User user);
    void updatePasswordOfUser(User user);
    void updateRoleOfUser(User user);
}