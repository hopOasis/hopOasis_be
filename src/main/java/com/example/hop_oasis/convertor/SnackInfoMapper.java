package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.SnackInfoDto;
import com.example.hop_oasis.model.Snack;
import com.example.hop_oasis.model.SnackImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SnackInfoMapper extends  Mappable<Snack,SnackInfoDto>{
    @Mapping(target = "snackImageName", source = "snackImage")
    SnackInfoDto toDto(Snack snack);

    default List<String> mapSnackImagesName(List<SnackImage> images){
        return images.stream()
                .map(SnackImage::getName)
                .collect(Collectors.toList());
    }
}
