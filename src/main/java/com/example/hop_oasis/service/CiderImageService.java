package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.dto.ImageUrlDto;
import org.springframework.web.multipart.MultipartFile;
public interface CiderImageService {
    ImageUrlDto getCiderImageByName(String name);
    CiderInfoDto addCiderImageToCider(Long ciderId, MultipartFile file);
    void deleteCiderImage(String name);
}
