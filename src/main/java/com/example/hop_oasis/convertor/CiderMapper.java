package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.CiderDto;
import com.example.hop_oasis.model.Cider;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CiderMapper extends Mappable<Cider, CiderDto>{
}
