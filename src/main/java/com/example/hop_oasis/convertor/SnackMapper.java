package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.SnackDto;
import com.example.hop_oasis.model.Snack;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SnackMapper extends Mappable<Snack, SnackDto>{
}
