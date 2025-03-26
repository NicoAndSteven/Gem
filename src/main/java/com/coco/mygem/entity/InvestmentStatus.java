package com.coco.mygem.entity;

public enum InvestmentStatus {
    PENDING("待确认"),
    CONFIRMED("已确认"),
    CANCELLED("已取消");

    private final String description;

    InvestmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static InvestmentStatus fromString(String status) {
        for (InvestmentStatus s : InvestmentStatus.values()) {
            if (s.name().equals(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("未知的投资状态: " + status);
    }
} 