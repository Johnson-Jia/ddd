package com.tbc.ddd.common.exception.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * 错误枚举列表
 *
 * @author Johnson.Jia
 */
@Slf4j
public enum ExceptionEnum {

    /**
     * 坏的请求 错误的请求 参数不完整 400
     */
    BAD_REQUEST(400, "Bad Request."),

    /**
     * 用户未授权，需要登录
     */
    UNAUTHORIZED(401, "User Unauthorized."),

    /**
     * 操作处理成功
     */
    SUCCESS(1000, "Business Success."),

    /**
     * 自定义弹窗处理 操作异常，请稍候重试
     */
    ALERT_ERROR(2000, "Business Error.");

    ExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
