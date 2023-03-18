package com.tbc.ddd.common.spring;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Spring 根据线程标识获取 当前请求的session web request 项目中使用
 *
 * @author Johnson.Jia
 */
public interface ContextHolderUtils {
    /**
     * 获取 servlet request
     *
     * @author Johnson.Jia
     * @date 2023/3/18 18:32:21
     * @return
     */
    static HttpServletRequest getServletRequest() {
        return ((ServletRequestAttributes)Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
            .getRequest();
    }

    /**
     * 获取 servlet response
     *
     * @author Johnson.Jia
     * @date 2023/3/18 18:32:29
     * @return
     */
    static HttpServletResponse getServletResponse() {
        return ((ServletRequestAttributes)Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
            .getResponse();
    }

    /**
     * 获取 session
     *
     * @author Johnson.Jia
     * @date 2023/3/18 18:32:40
     * @return
     */
    static HttpSession getSession() {
        return getServletRequest().getSession();

    }

    /**
     * 获取session
     *
     * @author Johnson.Jia
     * @date 2023/3/18 18:33:11
     * @param create
     *            session 为空时是否创建新session true：创建 false：不创建
     * @return
     */
    static HttpSession getSession(boolean create) {
        return getServletRequest().getSession(create);

    }
}
