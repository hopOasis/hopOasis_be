package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleImageDto;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.ProductBundleImage;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductBundleMapper extends Mappable<ProductBundle, ProductBundleDto> {

  @Mapping(target = "imageDto", source = "productImage")
  ProductBundleDto toDto(ProductBundle entity);

  @Mapping(target = "productImage", source = "imageDto")
  ProductBundle toEntity(ProductBundleDto dto);

  List<ProductBundleDto> toDtos(List<ProductBundle> entities);

  List<ProductBundle> toEntities(List<ProductBundleDto> dtos);

  ProductBundleImageDto toProductBundleImageDto(ProductBundleImage entity);

  ProductBundleImage toProductBundleImage(ProductBundleImageDto dto);
}
