package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.CiderImage;
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
public interface CiderInfoMapper extends Mappable<Cider, CiderInfoDto> {

    @Mapping(target = "ciderImageName", source = "image")
    @Mapping(target = "specialOfferIds", source = "specialOfferProduct")
    CiderInfoDto toDto(Cider cider);

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
