package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.model.Beer;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BeerMapper extends Mappable<Beer, BeerDto> {




}
