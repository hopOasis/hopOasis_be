package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CiderDto;
import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.model.Cider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CiderService {
    Cider saveCider(CiderDto ciderDto);

    CiderInfoDto getCiderById(Long id);

    Page<CiderInfoDto> getAllCidersWithFilter(String ciderName, Pageable pageable, String sortDirection);

    CiderInfoDto addRatingAndReturnUpdatedCiderInfo(Long itemId, double ratingValue);

    CiderInfoDto update(CiderDto ciderDto, Long id);

    CiderInfoDto deleteCider(Long id);
}
