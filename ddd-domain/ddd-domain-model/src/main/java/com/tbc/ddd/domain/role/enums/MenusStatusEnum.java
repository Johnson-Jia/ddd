package com.tbc.ddd.domain.role.enums;

/**
 * 菜单状态枚举
 *
 * @author Johnson.Jia
 * @date 2023/3/15 16:17:19
 */
public enum MenusStatusEnum {

    /**
     * 未知
     */
    UNKNOWN(-1, "未知"),
    /**
     * 禁用
     */
    DISABLE(0, "禁用"),
    /**
     * 启用
     */
    ENABLE(1, "启用");

    int code;
    String name;

    MenusStatusEnum(int code, String name) {
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

    public static MenusStatusEnum valueOf(int code) {
        for (MenusStatusEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
