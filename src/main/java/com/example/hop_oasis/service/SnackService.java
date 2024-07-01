package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.SnackDto;
import com.example.hop_oasis.dto.SnackInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SnackService {
    void saveSnack(MultipartFile file, SnackDto snackDto);
    SnackInfoDto getSnackById(Long id);
    Page<SnackInfoDto> getAllSnacks(Pageable pageable);
    void updateSnack(SnackInfoDto snackInfo, Long id);
    void deleteSnack(Long id);
}
