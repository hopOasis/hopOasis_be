package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.SnackInfoMapper;
import com.example.hop_oasis.convertor.SnackMapper;
import com.example.hop_oasis.convertor.SnackOptionsMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Snack;
import com.example.hop_oasis.model.SnackOptions;
import com.example.hop_oasis.repository.SnackOptionsRepository;
import com.example.hop_oasis.repository.SnackRepository;
import com.example.hop_oasis.service.SnackService;
import com.example.hop_oasis.utils.SnackSpecification;
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

import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class SnackServiceImpl implements SnackService {
    private final SnackRepository snackRepository;
    private final SnackMapper snackMapper;
    private final SnackInfoMapper snackInfoMapper;
    private final SnackRatingServiceImpl snackRatingService;
    private final SnackOptionsRepository snackOptionsRepository;
    private final SnackOptionsMapper snackOptionsMapper;

    @Override
    public Snack saveSnack(SnackDto snackDto) {
        Snack snack = snackMapper.toEntity(snackDto);
        List<SnackOptions> snackOptionsList = snackOptionsMapper.toEntity(snackDto.getOptions());
        for (SnackOptions options : snackOptionsList) {
            options.setSnack(snack);
        }
        snack.setSnackOptions(snackOptionsList);
        snackRepository.save(snack);
        return snack;
    }

    @Override
    public SnackInfoDto getSnackById(Long id) {
        Snack snack = snackRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return convertToDtoWithRating(snack);
    }

    @Override
    public Page<SnackInfoDto> getAllSnacksWithFilter(String snackName, Pageable pageable, String sortDirection) {
        Specification<Snack> specification = Specification.
                where(SnackSpecification.findByName(snackName)).
                and(SnackSpecification.sortByPrice(sortDirection));

        Page<Snack> entities = snackRepository.findAll(specification, pageable);
        return entities.map(this::convertToDtoWithRating);

    }
    @Override
    public SnackInfoDto addRatingAndReturnUpdatedSnackInfo(Long id, double ratingValue) {

        snackRatingService.addRating(id, ratingValue);
        Snack snack = snackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Snack not found with id " + id));
        return convertToDtoWithRating(snack);
    }

    private SnackInfoDto convertToDtoWithRating(Snack snack) {
        SnackInfoDto snackInfoDto = snackInfoMapper.toDto(snack);
        ItemRatingDto rating = snackRatingService.getItemRating(snack.getId());
        BigDecimal roundedAverageRating = BigDecimal.valueOf(rating.getAverageRating())
                .setScale(1, RoundingMode.HALF_UP);
        snackInfoDto.setAverageRating(roundedAverageRating.doubleValue());
        snackInfoDto.setRatingCount(rating.getRatingCount());
        return snackInfoDto;
    }


    @Override
    @Transactional
    public SnackInfoDto updateSnack(SnackDto snackDto, Long id) {
        Snack snack = snackRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));

        if (!snackDto.getSnackName().isEmpty()) {
            snack.setSnackName(snackDto.getSnackName());
        }

        if (Objects.nonNull(snackDto.getDescription())) {
            snack.setDescription(snackDto.getDescription());
        }
        List<SnackOptions> currentOptions = snack.getSnackOptions();
        List<SnackOptions> newOptions = snackOptionsMapper.toEntity(snackDto.getOptions());

        Map<Double, SnackOptions> currentOptionsMap = currentOptions.stream()
                        .collect(Collectors.toMap(SnackOptions :: getWeight, Function.identity()));
        for (SnackOptions newOption : newOptions) {
            SnackOptions excitingOption = currentOptionsMap.get(newOption.getWeight());
            if (excitingOption != null) {
                excitingOption.setQuantity(newOption.getQuantity());
                excitingOption.setPrice(newOption.getPrice());
            } else {
                newOption.setSnack(snack);
                currentOptions.add(newOption);
            }
        }
        currentOptions.removeIf(option ->
                newOptions.stream().noneMatch(newOpt -> newOpt.getWeight().equals(option.getWeight())));

        snack.setSnackOptions(currentOptions);
        return snackInfoMapper.toDto(snackRepository.save(snack));
    }

    @Override
    @Transactional
    public SnackInfoDto deleteSnack(Long id) {
        Snack snack = snackRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        snackRepository.deleteById(id);
        return snackInfoMapper.toDto(snack);
    }
}
