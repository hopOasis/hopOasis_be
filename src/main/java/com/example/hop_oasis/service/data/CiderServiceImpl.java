package com.example.hop_oasis.service.data;
import com.example.hop_oasis.convertor.CiderInfoMapper;
import com.example.hop_oasis.convertor.CiderMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.service.CiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.RESOURCE_DELETED;
import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CiderServiceImpl implements CiderService {
    private final CiderRepository ciderRepository;
    private final CiderMapper ciderMapper;
    private final CiderInfoMapper ciderInfoMapper;
    private final CiderRatingServiceImpl ciderRatingService;

    @Override
    public Cider saveCider( CiderDto ciderDto) {
        Cider cider = ciderMapper.toEntity(ciderDto);
        ciderRepository.save(cider);
        return cider;
    }
    @Override
    public CiderInfoDto getCiderById(Long id) {
        Cider cider = ciderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return convertToDtoWithRating(cider);
    }
    @Override
    public CiderInfoDto addRatingAndReturnUpdatedCiderInfo(Long id, double ratingValue) {
        ciderRatingService.addRating(id, ratingValue);
        Cider cider = ciderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cider not found with id " + id));
        return convertToDtoWithRating(cider);
    }
    private CiderInfoDto convertToDtoWithRating(Cider cider) {
        CiderInfoDto ciderInfoDto = ciderInfoMapper.toDto(cider);
        ItemRatingDto rating = ciderRatingService.getItemRating(cider.getId());
        BigDecimal roundedAverageRating = BigDecimal.valueOf(rating.getAverageRating())
                .setScale(1, RoundingMode.HALF_UP);
        ciderInfoDto.setAverageRating(roundedAverageRating.doubleValue());
        ciderInfoDto.setRatingCount(rating.getRatingCount());
        return ciderInfoDto;
    }
    @Override
    public Page<CiderInfoDto> getAllCiders(Pageable pageable) {
        Page<Cider> cider = ciderRepository.findAll(pageable);
        if (cider.isEmpty()) {
            return Page.empty(pageable);
        }
        return cider.map(this::convertToDtoWithRating);
    }
    @Override
    public CiderInfoDto update(CiderInfoDto ciderInfo, Long id) {
        Cider cider = ciderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        if (!ciderInfo.getCiderName().isEmpty()) {
            cider.setCiderName(ciderInfo.getCiderName());
        }
        if (ciderInfo.getVolumeLarge() != 0.0) {
            cider.setVolumeLarge(ciderInfo.getVolumeLarge());
        }
        if (ciderInfo.getVolumeSmall() != 0.0) {
            cider.setVolumeSmall(ciderInfo.getVolumeSmall());
        }
        if (ciderInfo.getPriceLarge() != 0.0) {
            cider.setPriceLarge(ciderInfo.getPriceLarge());
        }
        if (ciderInfo.getPriceSmall() != 0.0) {
            cider.setPriceSmall(ciderInfo.getPriceSmall());
        }
        if (!ciderInfo.getDescription().isEmpty()) {
            cider.setDescription(ciderInfo.getDescription());
        }
        return ciderInfoMapper.toDto(ciderRepository.save(cider));
    }
    @Override
    public CiderInfoDto deleteCider(Long id) {
        Cider cider = ciderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        ciderRepository.deleteById(id);
        return ciderInfoMapper.toDto(cider);
    }
}
