package com.github.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class) //spring security를 임시로 꺼둘 때 사용합니다.
//@SpringBootApplication
public class CommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceApplication.class, args);
    }

}
