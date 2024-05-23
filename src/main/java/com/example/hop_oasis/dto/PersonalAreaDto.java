package com.example.hop_oasis.dto;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PersonalAreaDto {
    private Long id;
    private boolean deleteArea;
    private UserDto userDto;
    private UserProfileDto userProfileDto;


}
