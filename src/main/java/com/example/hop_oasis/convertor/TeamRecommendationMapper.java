package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.RecommendationRequestDto;
import com.example.hop_oasis.model.TeamRecommendation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;



@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamRecommendationMapper {
    TeamRecommendation toEntity(RecommendationRequestDto dto);

}
