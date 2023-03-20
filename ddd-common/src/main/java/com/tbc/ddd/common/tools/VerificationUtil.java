package com.tbc.ddd.common.tools;

import com.tbc.ddd.common.exception.BaseException;

/**
 * 校验工具类
 *
 * @author Johnson.Jia
 * @date 2023/3/18 20:59:16
 */
public class VerificationUtil {
    /**
     * 校验为 true 时抛出异常
     *
     * @author Johnson.Jia
     * @date 2023/3/20 10:56:18
     * @param expect
     * @param msg
     *            异常提醒消息
     * @return
     */
    public static void isTrue(boolean expect, String msg) {
        if (expect) {
            throw new BaseException(msg);
        }
    }

    /**
     * 校验为 true 时抛出异常
     *
     * @author Johnson.Jia
     * @date 2023/3/20 10:57:16
     * @param expect
     * @param exception
     *            异常提醒对象
     * @return
     */
    public static void isTrue(boolean expect, BaseException exception) {
        if (expect) {
            throw exception;
        }
    }

    /**
     * 校验为 false 时抛出异常
     *
     * @author Johnson.Jia
     * @date 2023/3/20 10:58:05
     * @param expect
     * @param msg
     * @return
     */
    public static void isFalse(boolean expect, String msg) {
        isTrue(!expect, msg);
    }

    /**
     * 校验为 false 时抛出异常
     *
     * @author Johnson.Jia
     * @date 2023/3/20 10:58:27
     * @param expect
     *            异常提醒消息
     * @param exception
     *            异常提醒对象
     * @return
     */
    public static void isFalse(boolean expect, BaseException exception) {
        isTrue(!expect, exception);
    }
}
