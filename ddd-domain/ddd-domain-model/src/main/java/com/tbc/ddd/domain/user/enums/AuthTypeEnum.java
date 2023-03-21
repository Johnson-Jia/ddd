package com.tbc.ddd.domain.user.enums;

/**
 * 授权类型
 *
 * @author Johnson.Jia
 * @date 2023/3/21 19:00:41
 */
public enum AuthTypeEnum {

    /**
     * 未知
     */
    UNKNOWN("未知"),

    // 钉钉授权
    DINDING("钉钉"),

    // 微信授权
    WECHAT("微信");

    private String name;

    AuthTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
