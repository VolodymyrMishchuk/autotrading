package com.mishchuk.autotrade.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    ROLE_USER(UserRoleConstants.USER),
    ROLE_ADMIN(UserRoleConstants.ADMIN),
    ROLE_SUPER_ADMIN(UserRoleConstants.SUPER_ADMIN);

    private final String value;

    public static class UserRoleConstants {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    }
}
