package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.RecommendationsDto;
import com.example.hop_oasis.service.advisor.Recommendations;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {BeerInfoMapper.class, CiderInfoMapper.class, SnackInfoMapper.class, ProductBundleInfoMapper.class})
public interface RecommendationsMapper extends Mappable<Recommendations, RecommendationsDto> {

}
