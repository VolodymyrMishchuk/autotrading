package com.mishchuk.autotrade.service.user.admin;

import com.mishchuk.autotrade.service.model.User;

public interface UserAdminService {
    void updateRoleOfUser(User user);
    void updateStatusOfUser(User user);
}