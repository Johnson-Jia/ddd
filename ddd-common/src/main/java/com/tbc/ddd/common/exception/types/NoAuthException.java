package com.tbc.ddd.common.exception.types;

import com.tbc.ddd.common.exception.BaseException;
import com.tbc.ddd.common.exception.enums.ExceptionEnum;

/**
 * 用户未登录异常
 *
 * @author Johnson
 */
public class NoAuthException extends BaseException {

    public NoAuthException() {
        super(ExceptionEnum.UNAUTHORIZED);
    }

    public NoAuthException(String msg) {
        super(ExceptionEnum.UNAUTHORIZED, msg);
    }

}
