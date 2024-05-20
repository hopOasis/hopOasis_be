package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BeerInfoMapper extends Mappable<Beer, BeerInfoDto> {
    @Mapping(target = "imageName", source = "image")
    BeerInfoDto toDto(Beer beer);

    default List<String> mapImagesToNames(List<Image> images) {
        return images.stream()
                .map(Image::getName)
                .collect(Collectors.toList());
    }
}
