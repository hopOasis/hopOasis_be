package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.CiderImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CiderInfoMapper extends Mappable<Cider, CiderInfoDto>{

    @Mapping(target = "ciderImageName", source = "image")
    CiderInfoDto toDto(Cider cider);

    default List<String> mapCiderImagesName(List<CiderImage> images){
        return images.stream()
                .map(CiderImage::getName)
                .collect(Collectors.toList());
    }
}
