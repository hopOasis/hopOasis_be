package com.example.hop_oasis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HopOasisApplication {

    public static void main(String[] args) {
        SpringApplication.run(HopOasisApplication.class, args);
    }

}
