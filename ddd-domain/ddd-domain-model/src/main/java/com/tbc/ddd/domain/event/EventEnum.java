package com.tbc.ddd.domain.event;

import com.tbc.ddd.common.ddd.EventInterface;

/**
 * 事件枚举
 *
 * @author Johnson.Jia
 * @date 2023/3/18 17:41:27
 */
public enum EventEnum implements EventInterface {

    /**
     * 默认 未知事件 {@link com.tbc.ddd.common.event.DefaultEventEnum}
     */
    UNKNOWN("UNKNOWN", "未知"),

    // 登录成功事件
    LOGIN_SUCCESS("LOGIN_SUCCESS", "登录成功");

    EventEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    private String key;

    private String name;

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
