package com.tbc.ddd.common.bean;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.tbc.ddd.common.exception.BaseException;
import com.tbc.ddd.common.exception.enums.ExceptionEnum;
import com.tbc.ddd.common.utils.Consts;

import lombok.Data;

/**
 * 返回结果封装
 *
 * @author Johnson.Jia
 */
@Data
@SuppressWarnings("serial")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 8632964679110167394L;
    /**
     * 错误提醒信息
     */
    private String msg;
    /**
     * 错误编号
     */
    private int code;
    /**
     * 业务返回值
     */
    private T bizResult;
    /**
     * uuid 标识
     */
    private String traceId;

    public Result() {
        this(ExceptionEnum.SUCCESS);
    }

    public Result(ExceptionEnum exception) {
        this(exception, null);
    }

    public Result(ExceptionEnum exceptionEnum, String msg) {
        this.msg = StringUtils.isBlank(msg) ? exceptionEnum.getMsg() : msg;
        this.code = exceptionEnum.getCode();
    }

    public Result(T bizResult) {
        this(ExceptionEnum.SUCCESS);
        this.bizResult = bizResult;
    }

    public Result(BaseException exception, String msg) {
        this.msg = StringUtils.isBlank(msg) ? exception.getMsg() : msg;
        this.code = exception.getCode();
    }

    public static Result ok() {
        return new Result();
    }

    public static <T> Result<T> ok(T bizResult) {
        return new Result(bizResult);
    }

    public static Result error(String msg) {
        return new Result(ExceptionEnum.ALERT_ERROR, msg);
    }

    public static Result error(BaseException exception) {
        return new Result(exception, null);
    }

    public static Result error(BaseException exception, String msg) {
        return new Result(exception, msg);
    }

    public static Result error(ExceptionEnum exceptionEnum) {
        return new Result(exceptionEnum, null);
    }

    public static Result error(ExceptionEnum exceptionEnum, String msg) {
        return new Result(exceptionEnum, msg);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{\"msg\":\"");
        sb.append(msg).append("\",\"code\":").append(code).append(",\"traceId\":\"")
            .append(traceId == null ? "" : traceId).append("\",\"bizResult\":");
        if (bizResult == null) {
            sb.append("\"\"}");
        } else {
            sb.append(Consts.objectToString(bizResult)).append("}");
        }
        return sb.toString();
    }

}
