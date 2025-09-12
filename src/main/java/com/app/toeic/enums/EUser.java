package com.app.toeic.enums;

public enum EUser {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    BLOCKED("BLOCKED");

    private String value;

    EUser(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
