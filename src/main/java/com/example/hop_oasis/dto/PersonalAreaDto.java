package com.example.hop_oasis.dto;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonalAreaDto {
    private Long id;
    private boolean deleteArea;
    private UserDto user;
    private UserProfileDto userProfile;


}
