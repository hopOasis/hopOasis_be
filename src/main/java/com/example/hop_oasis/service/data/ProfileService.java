package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.OrderForProfileMapper;
import com.example.hop_oasis.dto.OrderForProfileDto;
import com.example.hop_oasis.dto.ProfileInfoDto;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Order;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.repository.OrderRepository;
import com.example.hop_oasis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderForProfileMapper orderForProfileMapper;

    public ProfileInfoDto getProfileByUserId(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User not found", ""));
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderForProfileDto> orderDtos = orderForProfileMapper.toDto(orders);
        return new ProfileInfoDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(), orderDtos);

    }
}
