package com.technicaltest.fileprocessor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.technicaltest.fileprocessor.shared.web.HttpRequestInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private HttpRequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor).addPathPatterns("/person-details/file");
    }
}