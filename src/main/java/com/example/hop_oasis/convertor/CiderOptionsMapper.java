package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.CiderOptionsDto;
import com.example.hop_oasis.model.CiderOptions;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CiderOptionsMapper {
    CiderOptionsDto toDto(CiderOptions ciderOptions);
    List<CiderOptionsDto> toDto(List<CiderOptions> ciderOptions);
}
