package com.example.hop_oasis.utils;
import com.example.hop_oasis.dto.PaymentDataDto;
import com.example.hop_oasis.dto.PersonalAreaDto;
import com.example.hop_oasis.dto.UserDto;
import com.example.hop_oasis.dto.UserProfileDto;
import com.example.hop_oasis.model.PaymentData;
import com.example.hop_oasis.model.PersonalArea;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.model.UserProfile;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class Converter {

    static ModelMapper modelMapper;

    public static UserDto toUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public static UserProfileDto toUserProfileDto(UserProfile userProfile) {
        return modelMapper.map(userProfile, UserProfileDto.class);
    }

    public static PaymentDataDto toPaymentDataDto(PaymentData paymentData) {
        return modelMapper.map(paymentData, PaymentDataDto.class);
    }
    public static PersonalAreaDto toPersonalAreaDto(PersonalArea personalArea){
        return modelMapper.map(personalArea, PersonalAreaDto.class);
    }
}
