package com.example.hop_oasis.decoder;

import org.springframework.stereotype.Component;

@Component
public class ExtractImageName {
    public String extractName(String name) {
        if (name.startsWith("http://") || name.startsWith("https://")) {
            return name.substring(name.lastIndexOf('/') + 1);
        }else {
            return name;
        }
    }
}
