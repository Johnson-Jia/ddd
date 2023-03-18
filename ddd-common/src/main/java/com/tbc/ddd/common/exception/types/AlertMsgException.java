package com.tbc.ddd.common.exception.types;

import com.tbc.ddd.common.exception.BaseException;
import com.tbc.ddd.common.exception.enums.ExceptionEnum;

/**
 * 弹窗提醒异常
 *
 * @author Johnson.Jia
 * @date 2023/3/18 18:42:11
 */
public class AlertMsgException extends BaseException {

    public AlertMsgException() {
        super(ExceptionEnum.ALERT_ERROR);
    }

    public AlertMsgException(String msg) {
        super(ExceptionEnum.ALERT_ERROR, msg);
    }
}
