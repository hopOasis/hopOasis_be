package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ProductBundleOptionsDto;
import com.example.hop_oasis.model.ProductBundleOptions;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductBundleOptionsMapper {
    ProductBundleOptionsDto toDto(ProductBundleOptions productBundleOptions);
    List<ProductBundleOptionsDto> toDto(List<ProductBundleOptions> productBundleOptions);
}
