package com.tbc.ddd.mybatis;

import java.util.Collections;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * mybatis plus 代码生成器
 *
 * @author Johnson.Jia
 * @date 2023/3/14 19:10:40
 */
public class MybatisPlusGenerator {

    public static void main(String[] args) {
        String url = "jdbc:mysql://172.21.0.67:3306/ddd_test";
        String username = "root";
        String password = "123456";

        String finalProjectPath = "E://ddd/ddd-infrastructure";
        String moduleName = "role";
        // String[] tables = {"t_uc_login", "t_uc_account", "t_uc_user_info"};
        String[] tables = {"t_uc_role", "t_uc_role_menus"};
        String tablePrefix = "t_uc_";

        FastAutoGenerator.create(url, username, password).globalConfig(builder -> {
            builder.author("Johnson.Jia") // 设置作者
                .disableOpenDir() // 禁止打开输出目录
                .commentDate("yyyy-MM-dd HH:mm:ss") // 设置时间格式
                .outputDir(finalProjectPath + "/src/main/java"); // 指定输出目录
        }).packageConfig(builder -> {
            builder.parent("com.tbc.ddd.infrastructure") // 设置父包名
                .moduleName(moduleName) // 设置父包模块名
                .entity("entity") // 设置entity包名
                .pathInfo(Collections.singletonMap(OutputFile.xml,
                    finalProjectPath + "/src/main/resources/mapper/" + moduleName)); // 设置mapperXml生成路径

        }).strategyConfig(builder -> {
            builder.addInclude(tables) // 设置需要生成的表名
                .addTablePrefix(tablePrefix); // 设置过滤表前缀
            builder.mapperBuilder().mapperAnnotation(Mapper.class).enableFileOverride();

            builder.entityBuilder().enableTableFieldAnnotation().convertFileName(entityName -> entityName + "PO")
                // .enableFileOverride() // 开启覆盖已生成文件
                .enableLombok(); // 开启lombok
            builder.serviceBuilder().enableFileOverride();
        }).templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
            .execute();
    }

}
