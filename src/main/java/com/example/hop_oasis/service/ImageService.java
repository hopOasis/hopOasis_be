package com.example.hop_oasis.service;
import com.example.hop_oasis.dto.BeerInfoDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    BeerInfoDto addImageToBeer(Long beerId, MultipartFile file);
    void deleteImage(String name);
}
