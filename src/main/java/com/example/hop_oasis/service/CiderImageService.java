package com.example.hop_oasis.service;
import com.example.hop_oasis.dto.CiderImageUrlDto;
import org.springframework.web.multipart.MultipartFile;
public interface CiderImageService {
    CiderImageUrlDto getCiderImageByName(String name);
    CiderImageUrlDto addCiderImageToCider(Long ciderId, MultipartFile file);
    void deleteCiderImage(String name);
}
