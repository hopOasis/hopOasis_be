package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.SnackDto;
import com.example.hop_oasis.dto.SnackInfoDto;
import com.example.hop_oasis.model.Snack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SnackService {
    Snack saveSnack(SnackDto snackDto);
    SnackInfoDto getSnackById(Long id);
    SnackInfoDto addRatingAndReturnUpdatedSnackInfo(Long itemId, double ratingValue);
    Page<SnackInfoDto> getAllSnacks(Pageable pageable);

    SnackInfoDto updateSnack(SnackDto snackDto, Long id);

    SnackInfoDto deleteSnack(Long id);
}
