package com.example.hop_oasis.service.data;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.convertor.BeerMapper;
import com.example.hop_oasis.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final BeerInfoMapper beerInfoMapper;
    private final BeerRatingServiceImpl beerRatingService;
    @Override
    public Beer save( BeerDto beerDto)  {
        Beer beer = beerMapper.toEntity(beerDto);
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
    public Page<BeerInfoDto> getAllBeers(Pageable pageable) {
        Page<Beer> beers = beerRepository.findAll(pageable);
        if (beers.isEmpty()) {
            return Page.empty(pageable);
        }
        return beers.map(this::convertToDtoWithRating);
    }
    @Override
    public BeerInfoDto update(BeerInfoDto beerInfo, Long id) {
        Beer beer = beerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));

        if (!beerInfo.getBeerName().isEmpty()) {
            beer.setBeerName(beerInfo.getBeerName());
        }
        if (beerInfo.getVolumeLarge() != 0.0) {
            beer.setVolumeLarge(beerInfo.getVolumeLarge());
        }
        if (beerInfo.getVolumeLarge() != 0.0) {
            beer.setVolumeSmall(beerInfo.getVolumeSmall());
        }
        if (beerInfo.getVolumeSmall() != 0.0) {
            beer.setPriceLarge(beerInfo.getPriceLarge());
        }
        if (beerInfo.getPriceSmall() != 0.0) {
            beer.setPriceSmall(beerInfo.getPriceSmall());
        }
        if (Objects.nonNull(beerInfo.getDescription())) {
            beer.setDescription(beerInfo.getDescription());
        }
        if (Objects.nonNull(beerInfo.getBeerColor())) {
            beer.setBeerColor(beerInfo.getBeerColor());
        }
        return beerInfoMapper.toDto(beerRepository.save(beer));
    }
    @Override
    public BeerInfoDto delete(Long id) {
        Beer beer = beerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        beerRepository.deleteById(id);
        return beerInfoMapper.toDto(beer);
    }
}
