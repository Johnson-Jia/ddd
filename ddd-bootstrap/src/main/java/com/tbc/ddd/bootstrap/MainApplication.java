package com.tbc.ddd.bootstrap;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.tbc.base.config.auto.CacheAutoConfig;
import com.tbc.base.config.auto.DubboAutoConfig;
import com.tbc.base.spring.SpringInit;
import com.tbc.paas.common.utils.spring.SpringContextHolder;

/**
 * @author Johnson
 */
@EnableWebMvc
@EnableDubboConfig
@SpringBootApplication
@DubboComponentScan("com.tbc.ddd.*")
@Import({DubboAutoConfig.class, CacheAutoConfig.class, SpringInit.class, SpringContextHolder.class})
public class MainApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainApplication.class, args);
    }

}
