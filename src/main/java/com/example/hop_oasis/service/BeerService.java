package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.model.Beer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeerService {
     Beer save(BeerDto beerDto);
     BeerInfoDto getBeerById(Long id);
     Page<BeerInfoDto> filterByBeerName(String beerName, Pageable pageable);
     BeerInfoDto addRatingAndReturnUpdatedBeerInfo(Long itemId, double ratingValue);
     Page<BeerInfoDto> getAllBeers(Pageable pageable, String sort);

     BeerInfoDto update(BeerDto beerDto, Long id);

     BeerInfoDto delete(Long id);

}
