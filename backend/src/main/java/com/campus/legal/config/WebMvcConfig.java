package com.campus.legal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String p = uploadDir.replace("\\", "/");
        if (!p.endsWith("/")) {
            p = p + "/";
        }
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + p);
    }
}
