package com.context;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.function.Function;

/**
 * @author xiangjin.kong
 * @date 2021/12/14 17:26
 * @desc 配置初始化
 */
@Component
public class ConfigurationInitiationApplication implements ApplicationRunner {

    private static Properties properties;
    private static String projectPath;
    static {
        // 读取配置文件
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("generate.properties");
            projectPath = System.getProperty("user.dir");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        /**
         * 构建数据源
         */
        String url = properties.getProperty("spring.datasource.url");
        String name = properties.getProperty("spring.datasource.name");
        String password = properties.getProperty("spring.datasource.password");
        DataSourceConfig.Builder dataSourceBuilder = new DataSourceConfig.Builder(url, name, password);

        /**
         * 构建快速生成器
         */
        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(dataSourceBuilder);
        fastAutoGenerator.globalConfig(this::globalConfig);
        fastAutoGenerator.packageConfig(this::packageConfig);
        fastAutoGenerator.strategyConfig(this::strategyConfig);
        fastAutoGenerator.templateConfig((stringStringFunction, builder) -> new FreemarkerTemplateEngine());
        fastAutoGenerator.execute();

    }

    /**
     * 策略配置
     * @param builder
     */
    private void strategyConfig(StrategyConfig.Builder builder) {
        StrategyConfig.Builder strategyConfigBuilder = new StrategyConfig.Builder();
        String includeTables = properties.getProperty("spring.table.include");
        String tablePrefix = properties.getProperty("spring.table.prefix");
        if (StringUtils.isNotBlank(includeTables)) {
            strategyConfigBuilder.addInclude(StringUtils.split(includeTables, ","));
        }
        strategyConfigBuilder.addTablePrefix(tablePrefix);
    }

    /**
     * 全局配置
     */
    private void globalConfig(GlobalConfig.Builder builder) {
        String author = properties.getProperty("spring.global.author");
        boolean swaggerEnable = properties.getProperty("spring.global.swagger.enable") == null ? false : Boolean.parseBoolean(properties.getProperty("spring.global.swagger.enable"));
        boolean fileOverride = properties.getProperty("spring.global.file.override") == null ? false : Boolean.parseBoolean(properties.getProperty("spring.global.file.override"));

        if (fileOverride) {
            builder.fileOverride();
        }
        if (swaggerEnable) {
            builder.enableSwagger();
        }
        builder.author(author);
        builder.outputDir(projectPath + "/mybatis-auto-generator/temp");
    }

    /**
     * 包配置
     */
    public void packageConfig(PackageConfig.Builder builder) {
        String parent = properties.getProperty("spring.package.parent");
        String moduleName = properties.getProperty("spring.package.moduleName");
        String pathInfo = properties.getProperty("spring.package.pathInfo");
        builder.parent(parent);
        if (StringUtils.isNotBlank(moduleName)) {
            builder.moduleName(moduleName);
        }
        builder.pathInfo(Collections.singletonMap(OutputFile.mapperXml, projectPath + "/mybatis-auto-generator/temp/resources" + pathInfo)); // 设置mapperXml生成路径

    }
}
