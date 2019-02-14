package com.corkili.learningserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LearningServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningServerApplication.class, args);
    }
}
