package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ReviewMapper;
import com.example.hop_oasis.convertor.SnackInfoMapper;
import com.example.hop_oasis.convertor.SnackMapper;
import com.example.hop_oasis.convertor.SnackOptionsMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.BeerOptions;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.Review;
import com.example.hop_oasis.model.Snack;
import com.example.hop_oasis.model.SnackOptions;
import com.example.hop_oasis.repository.ReviewRepository;
import com.example.hop_oasis.repository.SnackOptionsRepository;
import com.example.hop_oasis.repository.SnackRepository;
import com.example.hop_oasis.utils.SnackSpecification;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class SnackServiceImpl {
    private final SnackRepository snackRepository;
    private final SnackMapper snackMapper;
    private final SnackInfoMapper snackInfoMapper;
    private final SnackRatingServiceImpl snackRatingService;
    private final SnackOptionsRepository snackOptionsRepository;
    private final SnackOptionsMapper snackOptionsMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

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

    public SnackInfoDto getSnackById(Long id) {
        Snack snack = snackRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return convertToDtoWithRating(snack);
    }

    public Page<SnackInfoDto> getAllSnacksWithFilter(String snackName, Pageable pageable, String sortDirection) {
        Page<Snack> snacks = snackRepository.findAll(SnackSpecification.filterAndSort(snackName, sortDirection), pageable);
        return snacks.map(this::convertToDtoWithRating);

    }

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
        List<Review> reviews = reviewRepository.findByItemIdAndItemType(snack.getId(), ItemType.SNACK);
        List<ReviewInfoDto> dtos = reviewMapper.toDtos(reviews);
        snackInfoDto.setReviews(dtos);
        return snackInfoDto;
    }


    @Transactional
    public SnackInfoDto updateSnack(SnackDto snackDto, Long id) {
        Snack snack = snackRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));

        if (Objects.nonNull(snackDto.getSnackName())) {
            snack.setSnackName(snackDto.getSnackName());
        }

        if (Objects.nonNull(snackDto.getDescription())) {
            snack.setDescription(snackDto.getDescription());
        }
        List<SnackOptions> currentOptions = snack.getSnackOptions();

        if (Objects.nonNull(snackDto.getOptions())) {
            List<SnackOptions> newOptions = snackOptionsMapper.toEntity(snackDto.getOptions());
            for (SnackOptions curren : currentOptions) {
                for (SnackOptions newOption : newOptions) {
                    if (curren.getId() == newOption.getId()) {
                        if (newOption.getWeight() != 0) {
                            curren.setWeight(newOption.getWeight());
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
            snack.setSnackOptions(currentOptions);
        }
        return snackInfoMapper.toDto(snackRepository.save(snack));
    }

    @Transactional
    public SnackInfoDto deleteSnack(Long id) {
        Snack snack = snackRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        snackRepository.deleteById(id);
        return snackInfoMapper.toDto(snack);
    }
}
