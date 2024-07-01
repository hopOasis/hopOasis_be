package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CiderDto;
import com.example.hop_oasis.dto.CiderInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CiderService {
    void saveCider (MultipartFile file, CiderDto ciderDto);
    CiderInfoDto getCiderById(Long id);
    Page<CiderInfoDto> getAllCiders(Pageable pageable);
    void update(CiderInfoDto ciderInfo, Long id);
    void deleteCider(Long id);
}
