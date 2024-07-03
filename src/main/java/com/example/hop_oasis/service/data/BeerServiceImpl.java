package com.example.hop_oasis.service.data;

import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.ImageDto;
import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.convertor.ImageMapper;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Image;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.convertor.BeerMapper;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.repository.ImageRepository;
import com.example.hop_oasis.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final ImageRepository imageRepository;
    private final BeerMapper beerMapper;
    private final BeerInfoMapper beerInfoMapper;
    private final ImageMapper imageMapper;
    private final ImageCompressor imageCompressor;

    @Override
    public Beer save(MultipartFile file, BeerDto beerDto)  {
        byte[] bytesIm;
        try {
            bytesIm = imageCompressor.compressImage(file.getBytes());
        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND,"");
        }
        Image image = Image.builder()
                .image(bytesIm)
                .name(file.getOriginalFilename())
                .build();
        List<ImageDto> images = new ArrayList<>();
        images.add(imageMapper.toDto(image));
        beerDto.setImage(images);
        Beer beer = beerMapper.toEntity(beerDto);
        beerRepository.save(beer);
        image.setBeer(beer);
        imageRepository.save(image);
        return beer;
    }
    @Override
    public BeerInfoDto getBeerById(Long id) {
        Beer beer = beerRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return beerInfoMapper.toDto(beer);
    }
    @Override
    public Page<BeerInfoDto> getAllBeers(Pageable pageable) {
        Page<Beer> beers = beerRepository.findAll(pageable);
        if (beers.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        return beers.map(beerInfoMapper::toDto);
    }
    @Override
    public BeerInfoDto update(BeerInfoDto beerInfo, Long id) {
        Beer beer = beerRepository.findById(id).orElseThrow(()->
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
      return  beerInfoMapper.toDto(beerRepository.save(beer));
    }
    @Override
    public BeerInfoDto delete(Long id) {
        Beer beer = beerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        beerRepository.deleteById(id);
        return beerInfoMapper.toDto(beer);
    }
}
