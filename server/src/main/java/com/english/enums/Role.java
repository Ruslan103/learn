package com.english.enums;

public enum Role {
    ROLE_GUEST, ROLE_USER, ROLE_ADMIN, ROLE_CREATOR;

    public static boolean isRole(String role) {
        return role.equals(ROLE_GUEST.name())
                || role.equals(ROLE_USER.name())
                || role.equals(ROLE_ADMIN.name())
                || role.equals(ROLE_CREATOR.name());
    }

    public static Role toRole(String role) {
        return switch (role) {
            case "ROLE_USER" -> ROLE_USER;
            case "ROLE_ADMIN" -> ROLE_ADMIN;
            case "ROLE_CREATOR" -> ROLE_CREATOR;
            default -> ROLE_GUEST;
        };
    }
}