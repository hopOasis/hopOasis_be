package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.SnackImageDto;
import com.example.hop_oasis.model.SnackImage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SnackImageMapper extends Mappable<SnackImage, SnackImageDto> {

}
