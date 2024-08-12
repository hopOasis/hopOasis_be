package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.ProductBundleImage;
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
public interface ProductBundleInfoMapper extends Mappable<ProductBundle,ProductBundleInfoDto>{
    @Mapping(target = "productImageName", source = "productImage")
    @Mapping(target = "specialOfferIds", source = "specialOfferProduct")
    ProductBundleInfoDto toDto(ProductBundle productBundle);

    default List<String> mapSnackImagesName(List<ProductBundleImage> images){
        if(images == null) {
            return null;
        }
        return images.stream()
                .map(ProductBundleImage::getName)
                .collect(Collectors.toList());
    }
    default List<Long> mapOffersToIds(List<SpecialOfferProduct> offers) {
        if(offers == null) {
            return null;
        }
        return offers.stream()
                .map(SpecialOfferProduct::getId)
                .collect(Collectors.toList());
    }

}
