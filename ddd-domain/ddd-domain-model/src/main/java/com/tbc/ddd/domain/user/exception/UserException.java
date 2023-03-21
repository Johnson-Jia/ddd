package com.tbc.ddd.domain.user.exception;

import com.tbc.ddd.common.exception.BaseException;

/**
 * 用户错误 异常
 *
 * @author Johnson.Jia
 */
public class UserException extends BaseException {

    public UserException(String msg) {
        super(msg);
    }

}
