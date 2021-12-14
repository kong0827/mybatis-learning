package com;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.context.ConfigurationInitiationApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

/**
 * @author kxj
 * @date 2021/11/3 00:30
 * @desc
 */
@SpringBootApplication
public class AutoGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoGeneratorApplication.class, args);
    }
}
