package com.tbc.ddd.common.event;

import com.tbc.ddd.common.ddd.EventInterface;

/**
 * 默认 事件枚举
 *
 * @author Johnson.Jia
 * @date 2023/3/18 17:41:27
 */
public enum DefaultEventEnum implements EventInterface {

    // 未知事件
    UNKNOWN("UNKNOWN", "未知");

    DefaultEventEnum(String key, String name) {
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
