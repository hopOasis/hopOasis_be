package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CiderDto;
import com.example.hop_oasis.dto.CiderInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CiderService {
    void saveCider (MultipartFile file, CiderDto ciderDto);
    CiderInfoDto getCiderById(Long id);
    List<CiderInfoDto> getAllCiders();
    void update(CiderInfoDto ciderInfo, Long id);
    void deleteCider(Long id);
}
