package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.SnackDto;
import com.example.hop_oasis.dto.SnackInfoDto;
import com.example.hop_oasis.model.Snack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SnackService {
    Snack saveSnack(SnackDto snackDto);
    SnackInfoDto getSnackById(Long id);
    Page<SnackInfoDto> getAllSnacksWithFilter(String snackName, Pageable pageable, String sortDirection);

    SnackInfoDto addRatingAndReturnUpdatedSnackInfo(Long itemId, double ratingValue);

    SnackInfoDto updateSnack(SnackDto snackDto, Long id);

    SnackInfoDto deleteSnack(Long id);
}
