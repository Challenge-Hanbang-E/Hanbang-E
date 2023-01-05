package com.hanbang.e;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HanbangEApplication {

    public static void main(String[] args) {
        SpringApplication.run(HanbangEApplication.class, args);
    }

}
