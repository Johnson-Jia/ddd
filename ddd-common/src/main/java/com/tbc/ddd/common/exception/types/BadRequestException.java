package com.tbc.ddd.common.exception.types;

import com.tbc.ddd.common.exception.BaseException;
import com.tbc.ddd.common.exception.enums.ExceptionEnum;

/**
 * 访问异常 抛出 400 坏的请求 参数不完整
 *
 * @author Johnson
 */
public class BadRequestException extends BaseException {

    public BadRequestException() {
        super(ExceptionEnum.BAD_REQUEST);
    }

    public BadRequestException(String msg) {
        super(ExceptionEnum.BAD_REQUEST, msg);
    }
}
