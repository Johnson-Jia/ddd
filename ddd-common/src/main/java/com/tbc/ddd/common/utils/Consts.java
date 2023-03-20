package com.tbc.ddd.common.utils;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
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
        OBJECT_MAPPER.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
    }

    /**
     * 字符串转 map 对象支持 多层嵌套 <br/>
     * 示例如下： <br/>
     *
     * Map<String, List<Role>> map = <br/>
     * readValueMap(str, getCollectionType(String.class), getCollectionType(List.class, Role.class));
     *
     * @author Johnson.Jia
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
