package com.vueespring.utils.CodeGe;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Collections;

public class CodeGenerator{
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/vue-spring?severTimezone=GMT%2B8&characterEncoding=UTF-8","root","password")
                .globalConfig(builder -> {
                    builder.author("Cyk")
                            .enableSwagger()
                            .outputDir("C:\\Users\\ARC\\Desktop\\JETB\\vuee-spring\\src\\main\\java\\com\\vueespring\\utils\\CodeGe");
                })
                .packageConfig(builder -> {
                    builder.parent("")
                            .entity("entity")
                            .service("service")
                            .serviceImpl("serviceImpl")
                            .mapper("mapper")
                            .xml("mapper.xml")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml,"src/main/resources/mapper"));
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
