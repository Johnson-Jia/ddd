package com.tbc.ddd.domain.event;

/**
 * 事件枚举
 *
 * @author Johnson.Jia
 * @date 2023/3/18 17:41:27
 */
public enum DomainEventEnum {
    // 未知事件
    UNKNOWN("unknown", "未知"),

    // 登录成功事件
    LOGIN_SUCCESS("login_success", "登录成功");

    DomainEventEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
