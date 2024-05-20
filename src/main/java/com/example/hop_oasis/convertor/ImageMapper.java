package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.ImageDto;
import com.example.hop_oasis.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper extends Mappable<Image, ImageDto> {

}
