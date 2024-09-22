package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.BeerOptionsDto;
import com.example.hop_oasis.model.BeerOptions;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BeerOptionsMapper {
    BeerOptionsDto toDto(BeerOptions beerOptions);

    List<BeerOptionsDto> toDto(List<BeerOptions> options);


}
