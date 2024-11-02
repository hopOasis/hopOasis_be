package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.CiderInfoMapper;
import com.example.hop_oasis.convertor.CiderMapper;
import com.example.hop_oasis.convertor.CiderOptionsMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.CiderOptions;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.service.CiderService;
import com.example.hop_oasis.utils.CiderSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_DELETED;
import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CiderServiceImpl implements CiderService {
    private final CiderRepository ciderRepository;
    private final CiderMapper ciderMapper;
    private final CiderInfoMapper ciderInfoMapper;
    private final CiderRatingServiceImpl ciderRatingService;
    private final CiderOptionsMapper ciderOptionsMapper;

    @Override
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

    @Override
    public CiderInfoDto getCiderById(Long id) {
        Cider cider = ciderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return convertToDtoWithRating(cider);
    }
    @Override
    public Page<CiderInfoDto> getAllCidersWithFilter(String ciderName, Pageable pageable, String sortDirection) {
        Specification<Cider> specification = Specification.
                where(CiderSpecification.findByName(ciderName)).
                and(CiderSpecification.sortByPrice(sortDirection));
        Page<Cider> ciders = ciderRepository.findAll(specification, pageable);

        return ciders.map(this::convertToDtoWithRating);
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
        List<CiderOptions> currentOptions = cider.getCiderOptions();
        List<CiderOptions> newOptions = ciderOptionsMapper.toEntity(ciderDto.getOptions());

        Map<Double, CiderOptions> currentOptionsMap = currentOptions.stream()
                        .collect(Collectors.toMap(CiderOptions::getVolume, Function.identity()));
        for (CiderOptions newOption : newOptions) {
            CiderOptions exitingOption = currentOptionsMap.get(newOption.getVolume());
            if (exitingOption != null) {
                exitingOption.setQuantity(newOption.getQuantity());
                exitingOption.setPrice(newOption.getPrice());
            } else {
                newOption.setCider(cider);
                currentOptions.add(newOption);
            }
        }
        currentOptions.removeIf(option ->
                newOptions.stream().noneMatch(newOpt -> newOpt.getVolume().equals(option.getVolume())));

        cider.setCiderOptions(currentOptions);
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
