package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.dto.ItemShortInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(source = "beerName", target = "name")
    ItemShortInfoDto beerToItemInfoDto(BeerInfoDto beerInfoDto);

    @Mapping(source = "ciderName", target = "name")
    ItemShortInfoDto ciderToItemInfoDto(CiderInfoDto ciderInfoDto);

}
