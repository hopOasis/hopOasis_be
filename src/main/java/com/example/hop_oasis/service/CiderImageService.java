package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CiderImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface CiderImageService {
    CiderImageDto getCiderImageByName(String name);
    void addCiderImageToCider(Long ciderId, MultipartFile file);
    void deleteCiderImage(String name);
}
