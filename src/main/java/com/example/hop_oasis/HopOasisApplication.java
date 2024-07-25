package com.example.hop_oasis;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.hop_oasis.service.data.S3Service;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HopOasisApplication {

    public static void main(String[] args) {
        SpringApplication.run(HopOasisApplication.class, args);
    }
    @Bean
    public ApplicationRunner applicationRunner(S3Service s3Service){
        return args -> {
            //log.info("Spring Boot AWS S3 integration...");

            try {
                var s3Object = s3Service.getFile("jvm.jpg");
                //  log.info(s3Object.getKey());
            } catch (AmazonS3Exception e) {
                // log.error(e.getMessage());
            }
        };
    }
}
