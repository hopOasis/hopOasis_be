package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.model.Beer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BeerMapper {
    private final ImageMapper imageMapper;

    public BeerDto fromEntity(Beer beer){

        return BeerDto.builder()
                .id(beer.getId())
                .beerName(beer.getBeerName())
                .volumeLarge(beer.getVolumeLarge())
                .volumeSmall(beer.getVolumeSmall())
                .priceLarge(beer.getPriceLarge())
                .priceSmall(beer.getPriceSmall())
                .description(beer.getDescription())
                .bearColor(beer.getBearColor())
                .image(imageMapper.fromEntity(beer.getImage()))
                .build();
    }
    public List<BeerDto> fromEntity(Iterable<Beer>beers){
        List<BeerDto> dtos = new ArrayList<>();
        beers.forEach(beer -> dtos.add(fromEntity(beer)));
        return dtos;
    }

    public Beer toEntity(BeerDto dto){
        return Beer.builder()
                .beerName(dto.getBeerName())
                .volumeLarge(dto.getVolumeLarge())
                .volumeSmall(dto.getVolumeSmall())
                .priceLarge(dto.getPriceLarge())
                .priceSmall(dto.getPriceSmall())
                .description(dto.getDescription())
                .bearColor(dto.getBearColor())
                .image(imageMapper.toEntity(dto.getImage()))
                .build();
    }

}
