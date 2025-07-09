package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.FedExRateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FedExRateMapper {

    @Mapping(target = "senderPostalCode", constant = "01001")
    @Mapping(target = "recipientPostalCode", source = "recipientPostalCode")
    @Mapping(target = "weight", source = "weight")
    @Mapping(target = "serviceType", constant = "STANDARD_OVERNIGHT")
    FedExRateRequestDto toRateRequestDto(double weight, String recipientPostalCode);
}
