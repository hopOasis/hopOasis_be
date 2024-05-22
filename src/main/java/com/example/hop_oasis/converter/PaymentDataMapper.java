package com.example.hop_oasis.converter;

import com.example.hop_oasis.model.PaymentData;
import com.example.hop_oasis.dto.PaymentDataDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentDataMapper {
    PaymentDataMapper INSTANCE = Mappers.getMapper(PaymentDataMapper.class);

    @Mapping(target = "userProfile", source = "userProfileDto")
    PaymentData toEntity(PaymentDataDto paymentDataDto);

    @Mapping(target = "userProfileDto", source = "userProfile")
    PaymentDataDto toDto(PaymentData paymentData);
}