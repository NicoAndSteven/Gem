package com.coco.mygem.entity;

public enum PostType {
    TECH_COLLABORATION("技术协作"),
    FLASH_ACTIVITY("快闪活动"),
    CREATIVE_CROWD("创意众筹");

    private final String description;

    PostType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PostType fromString(String type) {
        for (PostType t : PostType.values()) {
            if (t.name().equals(type)) {
                return t;
            }
        }
        throw new IllegalArgumentException("未知的帖子类型: " + type);
    }
} 