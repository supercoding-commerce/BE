package com.github.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling //@Scheduled 위해 필요
@EnableJpaAuditing //@CreatedAt 위해 필요
//@SpringBootApplication(exclude = SecurityAutoConfiguration.class) //spring security를 임시로 꺼둘 때 사용합니다.
@SpringBootApplication
public class CommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceApplication.class, args);
    }

}
