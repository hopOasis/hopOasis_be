package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.RecommendationsDto;
import com.example.hop_oasis.model.Recommendations;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecommendationsMapper extends Mappable<Recommendations, RecommendationsDto> {

}
