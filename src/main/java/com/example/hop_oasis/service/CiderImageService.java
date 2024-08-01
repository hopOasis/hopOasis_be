package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CiderInfoDto;
import org.springframework.web.multipart.MultipartFile;
public interface CiderImageService {
    CiderInfoDto addCiderImageToCider(Long ciderId, MultipartFile file);
    void deleteCiderImage(String name);
}
