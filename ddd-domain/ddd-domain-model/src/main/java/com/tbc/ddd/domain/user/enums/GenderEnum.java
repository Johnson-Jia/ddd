package com.tbc.ddd.domain.user.enums;

/**
 * 性别枚举
 *
 * @author Johnson.Jia
 * @date 2023/3/16 14:15:53
 */
public enum GenderEnum {
    /**
     * 未知
     */
    UNKNOWN(-1, "未知"),
    /**
     * 女性
     */
    WOMAN(0, "女"),
    /**
     * 男性
     */
    MAN(1, "男");

    int code;

    String name;

    GenderEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static GenderEnum valueOf(int code) {
        for (GenderEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
