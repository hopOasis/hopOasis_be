package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.CiderImage;
import com.example.hop_oasis.model.SpecialOfferProduct;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CiderInfoMapper extends Mappable<Cider, CiderInfoDto> {

  @Mapping(target = "ciderImageName", source = "image")
  @Mapping(target = "specialOfferIds", source = "specialOfferProduct")
  CiderInfoDto toDto(Cider cider);

  default List<String> mapCiderImagesName(List<CiderImage> images) {
    return images.stream()
        .map(CiderImage::getName)
        .collect(Collectors.toList());
  }

  default List<Long> mapOffersToIds(List<SpecialOfferProduct> offers) {
    return offers.stream()
        .map(SpecialOfferProduct::getId)
        .collect(Collectors.toList());
  }
}
