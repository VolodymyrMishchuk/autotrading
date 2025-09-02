package com.mishchuk.autotrade.service.user.profile;

import com.mishchuk.autotrade.service.model.User;
import java.util.List;
import java.util.UUID;

public interface UserProfileService {
    User getUserById(UUID id);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(UUID id);
}