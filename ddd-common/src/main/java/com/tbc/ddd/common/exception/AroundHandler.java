package com.tbc.ddd.common.exception;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.dao.DataAccessException;

import com.tbc.ddd.common.bean.Result;
import com.tbc.ddd.common.exception.enums.ExceptionEnum;
import com.tbc.ddd.common.exception.types.BadRequestException;
import com.tbc.ddd.common.exception.types.NoAuthException;
import com.tbc.ddd.common.spring.ContextHolderUtils;
import com.tbc.ddd.common.tools.UserAgentUtils;
import com.tbc.ddd.common.utils.WebUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller 异常 AOP 统一处理
 * <p>
 * 默认规则只处理 Result {@link Result} 返回值 并且 Controller 包下
 * <p>
 * 声明这是一个组件
 * <p>
 * 声明这是一个切面Bean
 *
 * @author Johnson.Jia
 */
@Slf4j
@Aspect
public class AroundHandler {

    protected static final String ERROR =
        "【========请求处理异常========】---[{}]--->>>[{\"Model\":\"{}\",\"IP\":\"{}\",\"Version\":\"{}\""
            + ",\"CorpCode\":\"{}\",\"UID\":\"{}\"}] [{}]";
    /**
     * 请求跟踪标识 trace id
     */
    public static final String TRACE_ID = "DDD-TRACE-ID";
    /**
     * 客户端 版本号
     */
    public static final String VERSION = "version";

    /**
     * 客户端标识
     */
    public static final String USER_AGENT = "User-Agent";

    public void init() {
        UserAgentUtils.init();
    }

    /**
     * 配置环绕通知,使用在方法aspect()上注册的切入点
     * <p>
     * {@link com.tbc.ddd.common.bean.Result }
     *
     * @param joinPoint
     *            代理对象
     * @return ResultBean
     * @author Johnson.Jia
     */
    @Around("execution(com.tbc.ddd.common.bean.Result com.tbc..*(..)) && ("
        + "@annotation(org.springframework.web.bind.annotation.RequestMapping) || "
        + "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || "
        + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
        + "@annotation(org.springframework.web.bind.annotation.GetMapping) )")
    public Result around(ProceedingJoinPoint joinPoint) throws Throwable {
        Result result;
        String corpCode = null;
        String userId = null;
        HttpServletRequest request = ContextHolderUtils.getServletRequest();
        HttpServletResponse response = ContextHolderUtils.getServletResponse();
        String modelInfo = UserAgentUtils.getModelInfo(request.getHeader(USER_AGENT));
        String requestUri = request.getRequestURI();
        String ip = WebUtils.getNgigxAddress(request);
        String version = request.getHeader("-" + VERSION);
        String traceId = request.getHeader(TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        try {
            long time = System.currentTimeMillis();

            authCheck(joinPoint, request, response, time);
            result = (Result)joinPoint.proceed();
            long duration = System.currentTimeMillis() - time;
            log.info("[PV] Time(ms):" + duration + "  [{\"Model\":\"" + modelInfo + "\",\"IP\":\"" + ip
                + "\",\"Version\":\"" + version + "\",\"CorpCode\":\"" + corpCode + "\"" + ",\"UID\":\"" + userId
                + "\",\"URL\":\"" + requestUri + "\"}]");
        } catch (NoAuthException e) { // 未登录异常
            response.setStatus(ExceptionEnum.UNAUTHORIZED.getCode());
            result = Result.error(e);
        } catch (BadRequestException e) { // 请求参数不完整
            response.setStatus(ExceptionEnum.BAD_REQUEST.getCode());
            result = Result.error(e);
        } catch (Throwable e) {
            log.error(ERROR, requestUri, modelInfo, ip, version, corpCode, userId, error(joinPoint, request), e);
            throw e;
        } finally {
            // 清空上下文
        }
        if (result != null) {
            result.setTraceId(traceId);
        }
        return result;
    }

    /**
     * 认证 权限检查校验
     *
     * @param joinPoint
     *            代理对象
     * @param request
     *            http request请求对象
     * @author Johnson.Jia
     */
    private void authCheck(ProceedingJoinPoint joinPoint, HttpServletRequest request, HttpServletResponse response,
        long time) throws Exception {

    }

    /**
     * @param sessionId
     *            会话标识
     * @param request
     *            http 请求对象
     * @author Johnson.Jia
     */
    public void isLoginSignCheck(String sessionId, HttpServletRequest request, long time) throws Exception {
        if (StringUtils.isBlank(sessionId)) {
            throw new NoAuthException();
        }

    }

    /**
     * 错误日志 打印
     */
    public static Map<Object, Object> error(ProceedingJoinPoint joinPoint, HttpServletRequest request) {
        Map<Object, Object> result = new HashMap<>(16);
        Class<?>[] parameterTypes = ((MethodSignature)joinPoint.getSignature()).getMethod().getParameterTypes();
        int i = 0;
        for (Object object : joinPoint.getArgs()) {
            if (object instanceof HttpServletResponse || object instanceof HttpServletRequest) {
                continue;
            }
            result.put(parameterTypes[i], object);
        }
        Map<String, String> requestParams = WebUtils.getRequestParams(request);
        if (!requestParams.isEmpty()) {
            result.putAll(requestParams);
        }
        return result;
    }

}
