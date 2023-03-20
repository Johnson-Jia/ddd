package com.tbc.ddd.domain.user.exception;

import com.tbc.ddd.common.exception.BaseException;

/**
 * 密码错误 异常
 *
 * @author Johnson.Jia
 * @date 2023/3/17 18:03:23
 */
public class PasswordException extends BaseException {

    public PasswordException(String msg) {
        super(msg);
    }

}
