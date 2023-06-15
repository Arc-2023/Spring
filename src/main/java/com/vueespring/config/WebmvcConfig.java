package com.vueespring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebmvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/files/**").addResourceLocations(
                    "file:src/main/resources/static/files/","file:static/","classpath:/static/","classpath:src/main/resources/static/","file:src/main/resources/static/");
    }

}
