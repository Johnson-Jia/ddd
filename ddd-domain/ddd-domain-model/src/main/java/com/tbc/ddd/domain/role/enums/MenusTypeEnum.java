package com.tbc.ddd.domain.role.enums;

/**
 * 菜单类型枚举
 *
 * @author Johnson.Jia
 * @date 2023/3/16 14:35:13
 */
public enum MenusTypeEnum {

    /**
     * 未知
     */
    UNKNOWN(-1, "未知"),

    /**
     * 菜单
     */
    MENUS(0, "菜单"),

    /**
     * 功能点 按钮等
     */
    BUTTON(1, "按钮");

    int code;
    String name;

    MenusTypeEnum(int code, String name) {
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

    public static MenusTypeEnum valueOf(int code) {
        for (MenusTypeEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
