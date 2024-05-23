package com.example.hop_oasis.converter;

import com.example.hop_oasis.model.UserProfile;
import com.example.hop_oasis.dto.UserProfileDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserProfileMapper {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserProfileDto toDto(UserProfile userProfile);

    UserProfile toEntity(UserProfileDto userProfileDto);
}
