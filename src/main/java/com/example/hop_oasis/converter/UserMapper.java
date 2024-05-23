package com.example.hop_oasis.converter;

import com.example.hop_oasis.model.User;
import com.example.hop_oasis.dto.UserDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);
}
