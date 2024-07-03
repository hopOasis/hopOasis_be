package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CiderImageDto;
import com.example.hop_oasis.model.CiderImage;
import org.springframework.web.multipart.MultipartFile;

public interface CiderImageService {
    CiderImageDto getCiderImageByName(String name);
    CiderImage addCiderImageToCider(Long ciderId, MultipartFile file);
    void deleteCiderImage(String name);
}
