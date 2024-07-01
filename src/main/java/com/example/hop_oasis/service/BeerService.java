package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.model.Beer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BeerService {
     Beer save(MultipartFile file, BeerDto beerDto);
     BeerInfoDto getBeerById(Long id);
     Page<BeerInfoDto> getAllBeers(Pageable pageable);
     BeerInfoDto update(BeerInfoDto beerInfo,Long id);
     void delete(Long id);

}
