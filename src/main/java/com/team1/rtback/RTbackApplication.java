package com.team1.rtback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RTbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(RTbackApplication.class, args);
    }

}
