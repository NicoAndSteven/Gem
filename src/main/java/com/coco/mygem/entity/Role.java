package com.coco.mygem.entity;

public enum Role {
    ROLE_USER("普通用户"),
    ROLE_PUBLISHER("发布者"),
    ROLE_REVIEWER("审核员"),
    ROLE_ADMIN("管理员"),
    ROLE_INVESTOR("投资者");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.name().equals(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("未知的角色类型: " + role);
    }
} 