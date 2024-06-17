package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.model.ProductBundle;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductBundleMapper extends Mappable<ProductBundle, ProductBundleDto> {
}
