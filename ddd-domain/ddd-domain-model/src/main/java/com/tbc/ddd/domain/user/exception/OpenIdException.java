package com.tbc.ddd.domain.user.exception;

import com.tbc.ddd.common.exception.BaseException;

/**
 * openId 异常
 *
 * @author Johnson.Jia
 */
public class OpenIdException extends BaseException {

    public OpenIdException(String msg) {
        super(msg);
    }

}
