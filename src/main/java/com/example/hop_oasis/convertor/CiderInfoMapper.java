package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.dto.ItemInfoDto;
import com.example.hop_oasis.dto.ItemOptionsDto;
import com.example.hop_oasis.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CiderInfoMapper extends Mappable<Cider, CiderInfoDto> {

    @Mapping(target = "ciderImageName", source = "image")
    @Mapping(target = "specialOfferIds", source = "specialOfferProduct")
    @Mapping(target = "options", source = "ciderOptions")
    CiderInfoDto toDto(Cider cider);

    @Mapping(target = "measureValue", source = "volume")
    ItemOptionsDto toDto(CiderOptions ciderOptions);

    @Mapping(target = "imageName", source = "cider.image")
    @Mapping(target = "specialOfferIds", source = "cider.specialOfferProduct")
    @Mapping(target = "options", source = "cider.ciderOptions")
    @Mapping(target = "name", source = "cider.ciderName")
    @Mapping(target = "itemType", constant = "CIDER")
    ItemInfoDto mapToItemInfoDto(Cider cider);


    default List<String> mapCiderImagesName(List<CiderImage> images) {
        if (images == null) {
            return null;
        }
        return images.stream()
                .map(CiderImage::getName)
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
