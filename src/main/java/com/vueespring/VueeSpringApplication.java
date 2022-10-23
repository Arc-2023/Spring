package com.vueespring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.vueespring.mapper")
public class VueeSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueeSpringApplication.class, args);
    }

}
