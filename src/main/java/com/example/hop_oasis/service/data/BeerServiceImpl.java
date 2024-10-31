package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.BeerOptionsMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.convertor.BeerMapper;
import com.example.hop_oasis.service.BeerService;
import com.example.hop_oasis.utils.BeerSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final BeerInfoMapper beerInfoMapper;
    private final BeerRatingServiceImpl beerRatingService;
    private final BeerOptionsMapper beerOptionsMapper;

    @Override
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

    @Override
    public BeerInfoDto getBeerById(Long id) {
        Beer beer = beerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return convertToDtoWithRating(beer);
    }

    @Override
    public Page<BeerInfoDto> getAllBeersWithFilter(String beerName, Pageable pageable, String sortDirection) {
        Specification<Beer> specification = Specification.
                where(BeerSpecification.findByName(beerName)).
                and(BeerSpecification.sortByPrice(sortDirection));
        Page<Beer> beers = beerRepository.findAll(specification, pageable);

        return beers.map(this::convertToDtoWithRating);
    }

    @Override
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
        return beerInfoDto;
    }

    @Override
    @Transactional
    public BeerInfoDto update(BeerDto beerDto, Long id) {
        Beer beer = beerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));

        if (!beerDto.getBeerName().isEmpty()) {
            beer.setBeerName(beerDto.getBeerName());
        }

        if (Objects.nonNull(beerDto.getDescription())) {
            beer.setDescription(beerDto.getDescription());
        }

        if (Objects.nonNull(beerDto.getBeerColor())) {
            beer.setBeerColor(beerDto.getBeerColor());
        }

        List<BeerOptions> currentOptions = beer.getBeerOptions();
        List<BeerOptions> newOptions = beerOptionsMapper.toEntity(beerDto.getOptions());

        Map<Double, BeerOptions> currentOptionsMap = currentOptions.stream()
                .collect(Collectors.toMap(BeerOptions::getVolume, Function.identity()));

        for (BeerOptions newOption : newOptions) {
            BeerOptions existingOption = currentOptionsMap.get(newOption.getVolume());
            if (existingOption != null) {
                existingOption.setQuantity(newOption.getQuantity());
                existingOption.setPrice(newOption.getPrice());
            } else {
                newOption.setBeer(beer);
                currentOptions.add(newOption);
            }
        }

        currentOptions.removeIf(option ->
                newOptions.stream().noneMatch(newOpt -> newOpt.getVolume().equals(option.getVolume())));

        beer.setBeerOptions(currentOptions);
        return beerInfoMapper.toDto(beerRepository.save(beer));
    }

    @Override
    @Transactional
    public BeerInfoDto delete(Long id) {
        Beer beer = beerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        beerRepository.deleteById(id);
        return beerInfoMapper.toDto(beer);
    }
}
