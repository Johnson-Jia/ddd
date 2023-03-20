package com.tbc.ddd.common.exception;

import org.apache.commons.lang3.StringUtils;

import com.tbc.ddd.common.exception.enums.ExceptionEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统统一异常基类
 *
 * @author Johnson.Jia
 * @date 2023/3/17 17:59:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseException extends RuntimeException {

    /**
     * 错误 信息
     */
    private String msg;

    /**
     * 错误 编码
     */
    private int code;

    public BaseException(ExceptionEnum exceptionEnum) {
        this(exceptionEnum, null);
    }

    public BaseException(String msg) {
        this(ExceptionEnum.ALERT_ERROR, msg);
    }

    public BaseException(ExceptionEnum exceptionEnum, String msg) {
        this.code = exceptionEnum.getCode();
        this.msg = StringUtils.isBlank(msg) ? exceptionEnum.getMsg() : msg;
    }

    @Override
    public String getMessage() {
        if (StringUtils.isBlank(msg)) {
            return super.getMessage();
        }
        return msg;
    }
}
