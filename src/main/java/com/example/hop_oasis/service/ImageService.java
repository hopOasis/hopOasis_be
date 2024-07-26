package com.example.hop_oasis.service;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.ImageUrlDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageUrlDto getImageByName(String name);
    BeerInfoDto addImageToBeer(Long beerId, MultipartFile file);
    void deleteImage(String name);
}
