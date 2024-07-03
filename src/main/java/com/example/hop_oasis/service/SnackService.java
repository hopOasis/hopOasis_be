package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.SnackDto;
import com.example.hop_oasis.dto.SnackInfoDto;
import com.example.hop_oasis.model.Snack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SnackService {
    Snack saveSnack(MultipartFile file, SnackDto snackDto);
    SnackInfoDto getSnackById(Long id);
    Page<SnackInfoDto> getAllSnacks(Pageable pageable);
    SnackInfoDto updateSnack(SnackInfoDto snackInfo, Long id);
    SnackInfoDto  deleteSnack(Long id);
}
