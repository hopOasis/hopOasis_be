package com.example.hop_oasis.converter;

import com.example.hop_oasis.model.PersonalArea;
import com.example.hop_oasis.dto.PersonalAreaDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonalAreaMapper {
    PersonalAreaMapper INSTANCE = Mappers.getMapper(PersonalAreaMapper.class);

    @Mapping(target = "user", source = "userDto")
    @Mapping(target = "userProfile", source = "userProfileDto")
    PersonalArea toEntity(PersonalAreaDto personalAreaDto);

    @Mapping(target = "userDto", source = "user")
    @Mapping(target = "userProfileDto", source = "userProfile")
    PersonalAreaDto toDto(PersonalArea personalArea);
}
