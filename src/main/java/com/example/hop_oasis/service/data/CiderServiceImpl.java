package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.CiderInfoMapper;
import com.example.hop_oasis.convertor.CiderMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.CiderOptions;
import com.example.hop_oasis.repository.CiderOptionsRepository;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.service.CiderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_DELETED;
import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CiderServiceImpl implements CiderService {
    private final CiderRepository ciderRepository;
    private final CiderMapper ciderMapper;
    private final CiderInfoMapper ciderInfoMapper;
    private final CiderRatingServiceImpl ciderRatingService;
    private final CiderOptionsRepository ciderOptionsRepository;

    @Override
    public Cider saveCider(CiderDto ciderDto) {
        Cider cider = ciderMapper.toEntity(ciderDto);
        ciderRepository.save(cider);
        for (CiderOptionsDto optionsDto : ciderDto.getOptions()) {
            CiderOptions options = new CiderOptions();
            options.setCider(cider);
            options.setVolume(optionsDto.getVolume());
            options.setQuantity(optionsDto.getQuantity());
            options.setPrice(optionsDto.getPrice());
            ciderOptionsRepository.save(options);
        }
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
    @Transactional
    public CiderInfoDto update(CiderDto ciderDto, Long id) {
        Cider cider = ciderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        if (!ciderDto.getCiderName().isEmpty()) {
            cider.setCiderName(ciderDto.getCiderName());
        }
        if (Objects.nonNull(ciderDto.getDescription())) {
            cider.setDescription(ciderDto.getDescription());
        }
        for (CiderOptionsDto optionsDto : ciderDto.getOptions()) {
            CiderOptions options = new CiderOptions();
            options.setVolume(optionsDto.getVolume());
            options.setQuantity(optionsDto.getQuantity());
            options.setPrice(optionsDto.getPrice());
            ciderOptionsRepository.save(options);
        }
        return ciderInfoMapper.toDto(ciderRepository.save(cider));
    }

    @Override
    @Transactional
    public CiderInfoDto deleteCider(Long id) {
        Cider cider = ciderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        ciderRepository.deleteById(id);
        return ciderInfoMapper.toDto(cider);
    }
}
