package com.springboot.remember;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RememberApplication {

    public static void main(String[] args) {
        SpringApplication.run(RememberApplication.class, args);
    }
}
