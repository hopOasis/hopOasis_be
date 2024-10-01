package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.SnackOptionsDto;
import com.example.hop_oasis.model.SnackOptions;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SnackOptionsMapper {
    SnackOptionsDto toDto(SnackOptions snackOptions);
    List<SnackOptionsDto> toDto(List<SnackOptions> snackOptions);
}
