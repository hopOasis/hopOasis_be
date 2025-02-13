package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.CiderInfoMapper;
import com.example.hop_oasis.convertor.CiderMapper;
import com.example.hop_oasis.convertor.CiderOptionsMapper;
import com.example.hop_oasis.convertor.ReviewMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.CiderOptions;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.Review;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.repository.ReviewRepository;
import com.example.hop_oasis.utils.CiderSpecification;
import com.example.hop_oasis.utils.RequestValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_DELETED;
import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CiderServiceImpl {
    private final CiderRepository ciderRepository;
    private final CiderMapper ciderMapper;
    private final CiderInfoMapper ciderInfoMapper;
    private final CiderRatingServiceImpl ciderRatingService;
    private final CiderOptionsMapper ciderOptionsMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final RequestValidator requestValidator;

    public Cider saveCider(CiderDto ciderDto) {
        Cider cider = ciderMapper.toEntity(ciderDto);
        List<CiderOptions> ciderOptionsList = ciderOptionsMapper.toEntity(ciderDto.getOptions());
        for (CiderOptions options : ciderOptionsList) {
            options.setCider(cider);
        }
        cider.setCiderOptions(ciderOptionsList);
        ciderRepository.save(cider);
        return cider;
    }

    public CiderInfoDto getCiderById(Long id) {
        Cider cider = ciderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return convertToDtoWithRating(cider);
    }


    public Page<CiderInfoDto> getAllCidersWithFilter(String ciderName, Pageable pageable, String sortDirection,
                                                     Map<String, String> allParams) {
        List<String> params = List.of("ciderName", "sortDirection", "page", "size");
        String allowedParams = String.join(", ", params);
        requestValidator.validateParams(allParams, allowedParams);
        requestValidator.validateSortDirection(sortDirection);

        Page<Cider> ciders = ciderRepository.findAll(CiderSpecification.filterAndSort(ciderName, sortDirection), pageable);
        if (ciders.isEmpty() && ciderName != null) {
            throw new ResourceNotFoundException("Cider name not found", "");
        }
        return ciders.map(this::convertToDtoWithRating);
    }


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
        List<Review> reviews = reviewRepository.findByItemIdAndItemType(cider.getId(), ItemType.CIDER);
        List<ReviewInfoDto> dtos = reviewMapper.toDtos(reviews);
        ciderInfoDto.setReviews(dtos);
        return ciderInfoDto;
    }


    @Transactional
    public CiderInfoDto update(CiderDto ciderDto, Long id) {
        Cider cider = ciderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        if (Objects.nonNull(ciderDto.getCiderName())) {
            cider.setCiderName(ciderDto.getCiderName());
        }
        if (Objects.nonNull(ciderDto.getDescription())) {
            cider.setDescription(ciderDto.getDescription());
        }
        List<CiderOptions> currentOptions = cider.getCiderOptions();

        if (Objects.nonNull(ciderDto.getOptions())) {
            List<CiderOptions> newOptions = ciderOptionsMapper.toEntity(ciderDto.getOptions());
            for (CiderOptions curren : currentOptions) {
                for (CiderOptions newOption : newOptions) {
                    if (curren.getId() == newOption.getId()) {
                        if (Objects.nonNull(newOption.getVolume())) {
                            curren.setVolume(newOption.getVolume());
                        }
                        if (newOption.getQuantity() != 0) {
                            curren.setQuantity(newOption.getQuantity());
                        }
                        if (newOption.getPrice() != 0) {
                            curren.setPrice(newOption.getPrice());
                        }
                    }
                }

            }
            cider.setCiderOptions(currentOptions);

        }
        return ciderInfoMapper.toDto(ciderRepository.save(cider));
    }


    @Transactional
    public CiderInfoDto deleteCider(Long id) {
        Cider cider = ciderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        ciderRepository.deleteById(id);
        return ciderInfoMapper.toDto(cider);
    }
}
