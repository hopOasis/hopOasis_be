package com.example.hop_oasis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000/", "https://hop-oasis-fr.vercel.app/")
                .allowedMethods("GET", "POST");

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173/", "https://hopoasis-admin.vercel.app/")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
