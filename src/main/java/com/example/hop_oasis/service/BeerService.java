package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BeerService {
     void save(MultipartFile file,BeerDto beerDto);
     BeerInfoDto getBeerById(Long id);
     List<BeerInfoDto> getAllBeers();
     void update(BeerInfoDto beerInfo,Long id);
     void delete(Long id);

}
