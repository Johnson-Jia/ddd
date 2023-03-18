package com.tbc.ddd.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.tbc.ddd.common.exception.AroundHandler;

/**
 * AOP 切面 Exception 切面处理 自动装配
 *
 * @author Johnson.Jia
 */
@Configuration
@EnableConfigurationProperties({})
@Import({})
public class ExceptionAutoConfig {

    @Bean(initMethod = "init")
    AroundHandler exceptionHandler() {
        return new AroundHandler();
    }

}
