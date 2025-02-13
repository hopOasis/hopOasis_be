package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.BeerOptionsMapper;
import com.example.hop_oasis.convertor.ReviewMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.convertor.BeerMapper;
import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.ItemRatingDto;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.BeerOptions;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.Review;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.ReviewRepository;
import com.example.hop_oasis.utils.BeerSpecification;
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
public class BeerServiceImpl {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final BeerInfoMapper beerInfoMapper;
    private final BeerRatingServiceImpl beerRatingService;
    private final BeerOptionsMapper beerOptionsMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final RequestValidator requestValidator;


    public Beer save(BeerDto beerDto) {
        Beer beer = beerMapper.toEntity(beerDto);
        List<BeerOptions> beerOptionsList = beerOptionsMapper.toEntity(beerDto.getOptions());
        for (BeerOptions options : beerOptionsList) {
            options.setBeer(beer);
        }
        beer.setBeerOptions(beerOptionsList);
        beerRepository.save(beer);

        return beer;
    }


    public BeerInfoDto getBeerById(Long id) {
        Beer beer = beerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return convertToDtoWithRating(beer);
    }


    public Page<BeerInfoDto> getAllBeersWithFilter(String beerName, Pageable pageable, String sortDirection,
                                                   Map<String, String> allParams) {
        List<String> params = List.of("beerName", "sortDirection", "page", "size");
        String allowedParams = String.join(", ", params);
        requestValidator.validateParams(allParams, allowedParams);
        requestValidator.validateSortDirection(sortDirection);

        Page<Beer> beers = beerRepository.findAll(BeerSpecification.filterAndSort(beerName, sortDirection), pageable);
        if (beers.isEmpty() && beerName != null) {
            throw new ResourceNotFoundException("Beer name not found", "");
        }
        return beers.map(this::convertToDtoWithRating);
    }


    public BeerInfoDto addRatingAndReturnUpdatedBeerInfo(Long id, double ratingValue) {
        beerRatingService.addRating(id, ratingValue);
        Beer beer = beerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Beer not found with id " + id));
        return convertToDtoWithRating(beer);
    }

    private BeerInfoDto convertToDtoWithRating(Beer beer) {
        BeerInfoDto beerInfoDto = beerInfoMapper.toDto(beer);
        ItemRatingDto rating = beerRatingService.getItemRating(beer.getId());
        BigDecimal roundedAverageRating = BigDecimal.valueOf(rating.getAverageRating())
                .setScale(1, RoundingMode.HALF_UP);
        beerInfoDto.setAverageRating(roundedAverageRating.doubleValue());
        beerInfoDto.setRatingCount(rating.getRatingCount());

        List<Review> reviews = reviewRepository.findByItemIdAndItemType(beer.getId(), ItemType.BEER);
        List<ReviewInfoDto> dtos = reviewMapper.toDtos(reviews);
        beerInfoDto.setReviews(dtos);
        return beerInfoDto;
    }


    @Transactional
    public BeerInfoDto update(BeerDto beerDto, Long id) {
        Beer beer = beerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));

        if (Objects.nonNull(beerDto.getBeerName())) {
            beer.setBeerName(beerDto.getBeerName());
        }

        if (Objects.nonNull(beerDto.getDescription())) {
            beer.setDescription(beerDto.getDescription());
        }

        if (Objects.nonNull(beerDto.getBeerColor())) {
            beer.setBeerColor(beerDto.getBeerColor());
        }

        List<BeerOptions> currentOptions = beer.getBeerOptions();

        if (Objects.nonNull(beerDto.getOptions())) {
            List<BeerOptions> newOptions = beerOptionsMapper.toEntity(beerDto.getOptions());
            for (BeerOptions curren : currentOptions) {
                for (BeerOptions newOption : newOptions) {
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
            beer.setBeerOptions(currentOptions);

        }
        return beerInfoMapper.toDto(beerRepository.save(beer));
    }


    @Transactional
    public BeerInfoDto delete(Long id) {
        Beer beer = beerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        beerRepository.deleteById(id);
        return beerInfoMapper.toDto(beer);
    }
}
