package com.tbc.ddd;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * main 启动类
 *
 * @author Johnson
 */
@EnableAsync
@EnableDubbo
@SpringBootApplication
@MapperScan("com.tbc.ddd.infrastructure.*.mapper")
public class MainApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainApplication.class, args);
    }

}
