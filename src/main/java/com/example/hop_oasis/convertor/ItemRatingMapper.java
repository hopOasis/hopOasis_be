package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ItemRatingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemRatingMapper {

  @Mapping(source = "itemId", target = "itemId")
  @Mapping(source = "averageRating", target = "averageRating")
  @Mapping(source = "ratingCount", target = "ratingCount")
  ItemRatingDto toDto(Long itemId, double averageRating, int ratingCount);
}