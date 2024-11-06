package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.BeerOptionsDto;
import com.example.hop_oasis.model.BeerOptions;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BeerOptionsMapper extends Mappable<BeerOptions, BeerOptionsDto> {

}
