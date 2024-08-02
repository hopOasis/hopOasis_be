package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.CiderImageDto;
import com.example.hop_oasis.model.CiderImage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CiderImageMapper extends Mappable<CiderImage, CiderImageDto> {

}
