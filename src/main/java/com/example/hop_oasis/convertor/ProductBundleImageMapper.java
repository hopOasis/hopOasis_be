package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ProductBundleImageDto;
import com.example.hop_oasis.model.ProductBundleImage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductBundleImageMapper extends Mappable<ProductBundleImage, ProductBundleImageDto> {
}
