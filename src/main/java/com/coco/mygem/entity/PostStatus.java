package com.coco.mygem.entity;

public enum PostStatus {

    TEMP("0", "草稿"),
    PUSH("1", "待审批"),
    APPROVE("2", "审批中"),
    PUBLISHED("3", "已发布"),
    REJECTED("4", "被退回"),
    FINISH("5", "已结束");

    private String code;
    private String name;

    PostStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
