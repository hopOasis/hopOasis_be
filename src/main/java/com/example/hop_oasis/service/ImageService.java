package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.ImageDto;
import com.example.hop_oasis.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageDto getImageByName(String name);
    Image addImageToBeer(Long beerId, MultipartFile file);
    void deleteImage(String name);
}
