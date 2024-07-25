package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.SnackImageUrlDto;
import org.springframework.web.multipart.MultipartFile;

public interface SnackImageService {
    SnackImageUrlDto getSnackImageByName(String name);
    SnackImageUrlDto addSnackImageToSnack(Long snackId, MultipartFile file);
    void deleteSnackImage(String name);
}
