package com.context;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Properties;

/**
 * @author xiangjin.kong
 * @date 2021/12/14 17:26
 * @desc 配置初始化
 */
@Component
public class ConfigurationInitiationApplication implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 读取配置文件
        Properties properties = PropertiesLoaderUtils.loadAllProperties("generate.properties");

        /**
         * 构建数据源
         */
        String url = properties.getProperty("spring.datasource.url");
        String name = properties.getProperty("spring.datasource.name");
        String password = properties.getProperty("spring.datasource.password");
        DataSourceConfig.Builder dataSourceBuilder = new DataSourceConfig.Builder(url, name, password);


        /**
         * 全局配置
         */
        GlobalConfig.Builder globalConfigBuilder = new GlobalConfig.Builder();
        String author = properties.getProperty("spring.global.author");
        boolean swaggerEnable = properties.getProperty("spring.global.swagger.enable") == null ? false : Boolean.parseBoolean(properties.getProperty("spring.global.swagger.enable"));
        boolean fileOverride = properties.getProperty("spring.global.file.override") == null ? false : Boolean.parseBoolean(properties.getProperty("spring.global.file.override"));
        String fileLocation = properties.getProperty("spring.global.file.location");
        String projectPath = System.getProperty("user.dir");
        if (fileOverride) {
            globalConfigBuilder.fileOverride();
        }
        if (swaggerEnable) {
            globalConfigBuilder.enableSwagger();
        }
        globalConfigBuilder.author(author);
        globalConfigBuilder.outputDir(projectPath + "/mybatis-auto-generator/src/main/java/com/" + fileLocation);

        /**
         * 包配置
         */
        PackageConfig.Builder packConfigBuilder = new PackageConfig.Builder();
        String parent = properties.getProperty("spring.package.parent");
        String moduleName = properties.getProperty("spring.package.moduleName");
        String pathInfo = properties.getProperty("spring.package.pathInfo");
        packConfigBuilder.parent(parent);
        if (StringUtils.isNotBlank(moduleName)) {
            packConfigBuilder.moduleName(moduleName);
        }
        packConfigBuilder.pathInfo(Collections.singletonMap(OutputFile.mapperXml, projectPath + "/mybatis-auto-generator/resources/" + pathInfo)); // 设置mapperXml生成路径

        /**
         * 策略配置
         */
        StrategyConfig.Builder strategyConfigBuilder = new StrategyConfig.Builder();
        String includeTables = properties.getProperty("spring.table.include");
        String tablePrefix = properties.getProperty("spring.table.prefix");
        if (StringUtils.isNotBlank(includeTables)) {
            strategyConfigBuilder.addInclude(StringUtils.split(includeTables, ","));
        }
        strategyConfigBuilder.addTablePrefix(tablePrefix);

        /**
         * 构建快速生成器
         */
        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(dataSourceBuilder);
        fastAutoGenerator.packageConfig(builder -> packConfigBuilder.build());
        fastAutoGenerator.globalConfig(builder -> globalConfigBuilder.build());
        fastAutoGenerator.strategyConfig(builder -> strategyConfigBuilder.build());
        fastAutoGenerator.templateConfig((stringStringFunction, builder) -> new FreemarkerTemplateEngine());
        fastAutoGenerator.execute();

    }
}
