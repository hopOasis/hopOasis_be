package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ItemInfoDto;
import com.example.hop_oasis.dto.ItemOptionsDto;
import com.example.hop_oasis.dto.SnackInfoDto;
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
public interface SnackInfoMapper extends Mappable<Snack, SnackInfoDto> {
    @Mapping(target = "snackImageName", source = "snackImage")
    @Mapping(target = "specialOfferIds", source = "specialOfferProduct")
    @Mapping(target = "options", source = "snackOptions")
    SnackInfoDto toDto(Snack snack);

    @Mapping(target = "measureValue", source = "weight")
    ItemOptionsDto toDto(SnackOptions snackOptions);

    @Mapping(target = "imageName", source = "snack.snackImage")
    @Mapping(target = "specialOfferIds", source = "snack.specialOfferProduct")
    @Mapping(target = "options", source = "snack.snackOptions")
    @Mapping(target = "name", source = "snack.snackName")
    @Mapping(target = "itemType", constant = "SNACK")
    @Mapping(target = "averageRating", source = "averageRating")
    @Mapping(target = "ratingCount", source = "ratingCount")
    ItemInfoDto mapToItemInfoDto(Snack snack, double averageRating, int ratingCount);


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
