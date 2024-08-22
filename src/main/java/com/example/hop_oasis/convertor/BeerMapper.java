package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.model.Beer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;



@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BeerMapper extends Mappable<Beer, BeerDto> {




}
