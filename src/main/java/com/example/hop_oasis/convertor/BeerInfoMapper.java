package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.ItemInfoDto;
import com.example.hop_oasis.dto.ItemOptionsDto;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.BeerOptions;
import com.example.hop_oasis.model.Image;
import com.example.hop_oasis.model.SpecialOfferProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BeerInfoMapper extends Mappable<Beer, BeerInfoDto> {
    @Mapping(target = "imageName", source = "image")
    @Mapping(target = "specialOfferIds", source = "specialOfferProduct")
    @Mapping(target = "options", source = "beerOptions")
    BeerInfoDto toDto(Beer beer);

    @Mapping(target = "measureValue", source = "volume")
    ItemOptionsDto toDto(BeerOptions beerOptions);

    @Mapping(target = "imageName", source = "beer.image")
    @Mapping(target = "specialOfferIds", source = "beer.specialOfferProduct")
    @Mapping(target = "options", source = "beer.beerOptions")
    @Mapping(target = "name", source = "beer.beerName")
    @Mapping(target = "itemType", constant = "BEER")
    ItemInfoDto mapToItemInfoDto(Beer beer);


    default List<String> mapImagesToNames(List<Image> images) {
        if (images == null) {
            return null;
        }
        return images.stream()
                .map(Image::getName)
                .collect(Collectors.toList());
    }

    default List<Long> mapOffersToIds(List<SpecialOfferProduct> offers) {
        if (offers == null) {
            return null;
        }
        return offers.stream()
                .map(SpecialOfferProduct::getId)
                .collect(Collectors.toList());
    }
}
