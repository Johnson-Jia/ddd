package com.tbc.ddd.common.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 系统常量配置
 *
 * @author Johnson.Jia
 */
@Slf4j
public class Consts {

    /**
     * 用户信息 key
     */
    public static final String USER_INFO = "USER_INFO";

    /**
     * 是否允许 跳过检查 / 网关请求头 用户权限校验key
     */
    public static final String LOGIN_CHECK = "login-check";

    /**
     * session id
     */
    public static final String SESSION_SID = "-sid";
    /**
     * 客户端 版本号
     */
    public static final String VERSION = "-version";
    /**
     * 客户端当前时间戳
     */
    public static final String SESSION_TIME = "-time";
    /**
     * 客户端签名 参数
     */
    public static final String SESSION_SIGN = "-sign";
    /**
     * 客户端语言环境
     */
    public static final String LOCAL = "-local";
    /**
     * 客户端标识
     */
    public static final String USER_AGENT = "User-Agent";
    /**
     * 加密 字符串 规则 排序
     */
    public static final String SIGN_TEXT = "module={0}&method={1}&time={2}";
    /**
     * 客户端超时 处理
     */
    public static final long TIME = 2 * 60 * 1000L;

    public static final String CHARSET_UTF8 = StandardCharsets.UTF_8.name();

    /**
     * jackson 序列化
     */
    public static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        // 过滤掉 null值
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 关闭 把java.util.Date, Calendar输出为时间戳
        // OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 美化输出
        // OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        // 在遇到未知属性时防止异常
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }
    /**
     * 系统默认 sysconfig 配置
     */
    public static final String SYSCONFIG_PREFIX = "sysconfig";
    /**
     * redis config 配置
     */
    public static final String REDIS_CONFIG_PREFIX = "redis.config";
    /**
     * ehcache config 配置
     */
    public static final String EHCACHE_CONFIG_PREFIX = "ehcache.config";
    /**
     * 微信 config 配置
     */
    public static final String WECHAT_CONFIG_PREFIX = "wechat.config";
    /**
     * 百度 config 配置
     */
    public static final String BAIDU_CONFIG_PREFIX = "baidu.config";
    /**
     * 支付宝 支付
     */
    public static final String ALIPAY_PREFIX = "alipay.config";
    /**
     * redisson 限流器
     */
    public static final String RATE_LIMITER_PREFIX = "RATE_LIMITER_";

    /**
     * 字符串转 map 对象支持 多层嵌套 <br/>
     * 示例如下： <br/>
     *
     * Map<String, List<Role>> map = <br/>
     * readValueMap(str, getCollectionType(String.class), getCollectionType(List.class, Role.class));
     *
     * @author Johnson.Jia
     * @date 2020/7/30 18:06
     * @param content
     *            json 字符串
     * @param valueJavaType
     *            子对象解析类型
     * @return
     */
    public static <T> T readValueMap(String content, JavaType... valueJavaType) {
        return readValue(content, OBJECT_MAPPER.getTypeFactory().constructParametricType(Map.class, valueJavaType));
    }

    /**
     * 字符串转 对象支持 多层嵌套
     *
     * @author Johnson.Jia
     * @date 2020/7/30 18:06
     * @param content
     *            json 字符串
     * @param valueType
     *            子对象解析类型
     * @return
     */
    public static <T> T readValue(String content, JavaType valueType) {
        try {
            return OBJECT_MAPPER.readValue(content, valueType);
        } catch (IOException e) {
            log.error("【========jackson 序列化异常========】", e);
        }
        return null;
    }

    /**
     * 字符串转 对象
     *
     * @author Johnson.Jia
     * @date 2020/7/30 18:08
     * @param content
     *            字符串
     * @param parametrized
     *            对象class
     * @param parameterClasses
     *            子对象 class
     * @return
     */
    public static <T> T readValue(String content, Class<?> parametrized, Class<?>... parameterClasses) {
        try {
            if (parameterClasses == null || parameterClasses.length == 0) {
                return (T)OBJECT_MAPPER.readValue(content, parametrized);
            }
            return OBJECT_MAPPER.readValue(content, getCollectionType(parametrized, parameterClasses));
        } catch (IOException e) {
            log.error("【========jackson 序列化异常========】", e);
        }
        return null;
    }

    public static <T> T readValue(byte[] src, Class<?> parametrized, Class<?>... parameterClasses) {
        try {
            return OBJECT_MAPPER.readValue(src, getCollectionType(parametrized, parameterClasses));
        } catch (IOException e) {
            log.error("【========jackson 序列化异常========】", e);
        }
        return null;
    }

    public static String objectToString(Object object) {
        try {
            if (object == null) {
                return null;
            }
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            log.error("【========jackson 序列化异常========】", e);
        }
        return null;
    }

    /**
     * @param parametrized
     *            Actual full type
     * @param parameterClasses
     *            Type parameters to apply
     * @return
     * @author Johnson.Jia
     * @date 2019/4/9 16:02
     */
    public static JavaType getCollectionType(Class<?> parametrized, Class<?>... parameterClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }
}
