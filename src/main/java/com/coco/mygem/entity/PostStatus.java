package com.coco.mygem.entity;

public enum PostStatus {

    TEMP("0", "草稿"),
    PUSH("1", "待审批"),
    APPROVE("2", "审批中"),
    FINISH("3", "已结束"),
    REJECT("4", "被退回"),
    CANCEL("5", "已作废");

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
