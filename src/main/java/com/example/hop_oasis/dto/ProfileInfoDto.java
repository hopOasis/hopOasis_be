package com.example.hop_oasis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileInfoDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderForProfileDto> orders;
}
