package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.SnackInfoDto;
import com.example.hop_oasis.model.Snack;
import com.example.hop_oasis.model.SnackImage;
import com.example.hop_oasis.model.SpecialOfferProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SnackInfoMapper extends Mappable<Snack, SnackInfoDto> {
    @Mapping(target = "snackImageName", source = "snackImage")
    @Mapping(target = "specialOfferIds", source = "specialOfferProduct")
    SnackInfoDto toDto(Snack snack);

    default List<String> mapSnackImagesName(List<SnackImage> images) {
        if (images == null) {
            return null;
        }
        return images.stream()
                .map(SnackImage::getName)
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
