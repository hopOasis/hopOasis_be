package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.Image;
import com.example.hop_oasis.model.SpecialOfferProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;



import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BeerInfoMapper extends Mappable<Beer, BeerInfoDto> {
    @Mapping(target = "imageName", source = "image")
    @Mapping(target = "specialOfferIds", source = "specialOfferProduct")
    BeerInfoDto toDto(Beer beer);

    default List<String> mapImagesToNames(List<Image> images) {
        return images.stream()
                .map(Image::getName)
                .collect(Collectors.toList());
    }
    default List<Long> mapOffersToIds(List<SpecialOfferProduct> offers) {
        return offers.stream()
                .map(SpecialOfferProduct::getId)
                .collect(Collectors.toList());
    }
}
