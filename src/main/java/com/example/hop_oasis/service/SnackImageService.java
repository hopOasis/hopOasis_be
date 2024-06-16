package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.SnackImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface SnackImageService {
    SnackImageDto getSnackImageByName(String name);
    void addSnackImageToSnack(Long snackId, MultipartFile file);
    void deleteSnackImage(String name);
}
