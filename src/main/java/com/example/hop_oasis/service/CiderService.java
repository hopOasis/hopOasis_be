package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CiderDto;
import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.model.Cider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface CiderService {
    Cider saveCider (CiderDto ciderDto);
    CiderInfoDto getCiderById(Long id);
    CiderInfoDto addRatingAndReturnUpdatedCiderInfo(Long itemId, double ratingValue);
    Page<CiderInfoDto> getAllCiders(Pageable pageable);
    CiderInfoDto update(CiderInfoDto ciderInfo, Long id);
    CiderInfoDto deleteCider(Long id);
}
