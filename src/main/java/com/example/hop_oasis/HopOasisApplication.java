package com.example.hop_oasis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ImportRuntimeHints(value = GraalVmRuntimeHints.class)
public class HopOasisApplication {

    public static void main(String[] args) {
        SpringApplication.run(HopOasisApplication.class, args);
    }

}
