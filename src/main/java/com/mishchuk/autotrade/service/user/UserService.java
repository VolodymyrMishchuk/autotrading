package com.mishchuk.autotrade.service.user;

import com.mishchuk.autotrade.service.model.User;
import java.util.List;

public interface UserService {
    void createUser(User user);
    User getUser(String id);
    User getByToken(String token);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(String id);
    void completeRegistration(String token); // тут в Loyalty-card є ще password як аргумент
    User getAuthenticatedUser();
}