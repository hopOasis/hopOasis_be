package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface BeerService {
     void save(MultipartFile file,BeerDto beerDto) throws IOException;
     void addImageToBeer(Long beerId, MultipartFile file) throws IOException;
     BeerInfoDto getBeerById(Long id);
     List<BeerInfoDto> getAllBeers();
     void update(BeerInfoDto beerInfo,Long id);
     void delete(Long id);
    ImageDto getImageByName(String name) throws MalformedURLException;

}