package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ItemInfoDto;
import com.example.hop_oasis.dto.ItemOptionsDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
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
public interface ProductBundleInfoMapper extends Mappable<ProductBundle, ProductBundleInfoDto> {
    @Mapping(target = "productImageName", source = "productImage")
    @Mapping(target = "specialOfferIds", source = "specialOfferProduct")
    @Mapping(target = "options", source = "productBundleOptions")
    ProductBundleInfoDto toDto(ProductBundle productBundle);

    ItemOptionsDto toDto(ProductBundleOptions productBundleOptions);

    @Mapping(target = "imageName", source = "productBundle.productImage")
    @Mapping(target = "specialOfferIds", source = "productBundle.specialOfferProduct")
    @Mapping(target = "options", source = "productBundle.productBundleOptions")
    @Mapping(target = "name", source = "productBundle.name")
    @Mapping(target = "itemType", constant = "PRODUCT_BUNDLE")
    @Mapping(target = "averageRating", source = "averageRating")
    @Mapping(target = "ratingCount", source = "ratingCount")
    ItemInfoDto mapToItemInfoDto(ProductBundle productBundle, double averageRating, int ratingCount);


    default List<String> mapSnackImagesName(List<ProductBundleImage> images) {
        if (images == null) {
            return null;
        }
        return images.stream()
                .map(ProductBundleImage::getName)
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
