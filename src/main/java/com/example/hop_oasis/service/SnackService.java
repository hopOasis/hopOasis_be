package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.SnackDto;
import com.example.hop_oasis.dto.SnackInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SnackService {
    void saveSnack(MultipartFile file, SnackDto snackDto);
    SnackInfoDto getSnackById(Long id);
    List<SnackInfoDto> getAllSnacks();
    void updateSnack(SnackInfoDto snackInfo, Long id);
    void deleteSnack(Long id);
}
