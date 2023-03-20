package com.tbc.ddd.common.exception;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Objects;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tbc.ddd.common.bean.Result;
import com.tbc.ddd.common.exception.enums.ExceptionEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理
 *
 * @author Johnson
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    String MSG = "服务正在维护升级，请稍候重试";

    String ERROR = "XXX服务数据库异常";

    @ExceptionHandler(value = {BaseException.class})
    public Result baseExceptionHandler(BaseException e) {
        return Result.error(e);
    }

    @ExceptionHandler(value = {InvocationTargetException.class, RpcException.class})
    public Result rpcExceptionHandler(Exception e) {
        return Result.error(MSG);
    }

    @ExceptionHandler(value = {DataAccessException.class, SQLException.class})
    public Result dataExceptionHandler(Exception e) {
        return Result.error(ERROR);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public Result nullPointerExceptionHandler(NullPointerException e) {
        String message = e.getMessage();
        if (StringUtils.isBlank(message)) {
            message = e.getCause() != null ? e.getCause().getMessage() : "方法参数空指针异常";
        }
        return Result.error(message);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Result methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        return Result.error(Objects.requireNonNull(fieldError).getDefaultMessage());
    }

    /**
     * URL 参数校验 捕获异常处理
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        return Result.error(ExceptionEnum.BAD_REQUEST, e.getMessage());
    }

    /**
     * 注解 校验 自定义异常提醒
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public Result constraintViolationExceptionHandler(ConstraintViolationException e) {
        Iterator<ConstraintViolation<?>> iterator = e.getConstraintViolations().iterator();
        if (iterator.hasNext()) {
            return Result.error(iterator.next().getMessage());
        }
        return Result.error(e.getMessage());
    }

    /**
     * 非法参数
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public Result illegalArgumentExceptionHandler(IllegalArgumentException e) {
        return Result.error("非法参数--->" + e.getMessage());
    }

    /**
     * 请求方法不被该接口支持
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public Result httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        return Result.error("该接口不支持，请检查URL(" + e.getMessage() + ")");
    }

    @ExceptionHandler(value = {Throwable.class})
    public Result throwableHandler(Throwable e) {
        return Result.error(ExceptionEnum.ALERT_ERROR, e.getMessage());
    }

}
