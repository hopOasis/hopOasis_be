package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CiderDto;
import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.model.Cider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CiderService {
    Cider saveCider (MultipartFile file, CiderDto ciderDto);
    CiderInfoDto getCiderById(Long id);
    Page<CiderInfoDto> getAllCiders(Pageable pageable);
    CiderInfoDto update(CiderInfoDto ciderInfo, Long id);
    CiderInfoDto deleteCider(Long id);
}
