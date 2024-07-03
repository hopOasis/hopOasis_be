package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.SnackImageDto;
import com.example.hop_oasis.model.SnackImage;
import org.springframework.web.multipart.MultipartFile;

public interface SnackImageService {
    SnackImageDto getSnackImageByName(String name);
    SnackImage addSnackImageToSnack(Long snackId, MultipartFile file);
    void deleteSnackImage(String name);
}
