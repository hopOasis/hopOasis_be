package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.ImageUrlDto;

import com.example.hop_oasis.dto.SnackInfoDto;
import org.springframework.web.multipart.MultipartFile;

public interface SnackImageService {
    ImageUrlDto getSnackImageByName(String name);
    SnackInfoDto addSnackImageToSnack(Long snackId, MultipartFile file);
    void deleteSnackImage(String name);
}
