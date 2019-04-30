package com.corkili.learningserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.corkili.learningserver.common.ScormZipUtils;

@SpringBootApplication
@EnableCaching
public class it LearningServerApplication {

    public static void main(String[] args) {
        ScormZipUtils.setBasePath(args);
        SpringApplication.run(LearningServerApplication.class, args);
    }
}
