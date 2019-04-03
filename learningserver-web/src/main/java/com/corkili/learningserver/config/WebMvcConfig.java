package com.corkili.learningserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.common.ScormZipUtils;

@Configuration
@Slf4j
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("map static resource: [{}] -> [{}]", "/scormPackages/**", "file://" + ScormZipUtils.getBasePath());
        registry.addResourceHandler("/scormPackages/**")
                .addResourceLocations("file://" + ScormZipUtils.getBasePath());
    }
}
