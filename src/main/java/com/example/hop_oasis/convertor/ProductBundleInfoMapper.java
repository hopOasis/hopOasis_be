package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.ProductBundleImage;
import com.example.hop_oasis.model.SnackImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductBundleInfoMapper extends Mappable<ProductBundle,ProductBundleInfoDto>{
    @Mapping(target = "productImageName", source = "productImage")
    ProductBundleInfoDto toDto(ProductBundle productBundle);

    default List<String> mapSnackImagesName(List<ProductBundleImage> images){
        return images.stream()
                .map(ProductBundleImage::getName)
                .collect(Collectors.toList());
    }

}
