package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.model.Beer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BeerInfoMapper {
    public BeerInfoDto fromEntity(Beer beer){

        return BeerInfoDto.builder()
                .id(beer.getId())
                .beerName(beer.getBeerName())
                .volumeLarge(beer.getVolumeLarge())
                .volumeSmall(beer.getVolumeSmall())
                .priceLarge(beer.getPriceLarge())
                .priceSmall(beer.getPriceSmall())
                .description(beer.getDescription())
                .bearColor(beer.getBearColor())
                .build();
    }
    public List<BeerInfoDto> fromEntity(Iterable<Beer>beers){
        List<BeerInfoDto> dtos = new ArrayList<>();
        beers.forEach(beer -> dtos.add(fromEntity(beer)));
        return dtos;
    }

    public Beer toEntity(BeerInfoDto dto){
        return Beer.builder()
                .beerName(dto.getBeerName())
                .volumeLarge(dto.getVolumeLarge())
                .volumeSmall(dto.getVolumeSmall())
                .priceLarge(dto.getPriceLarge())
                .priceSmall(dto.getPriceSmall())
                .description(dto.getDescription())
                .bearColor(dto.getBearColor())
                .build();
    }
}
