package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.OrderForProfileDto;
import com.example.hop_oasis.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderForProfileMapper {
    @Mapping(target = "items", source = "orderItems")
    OrderForProfileDto toDto(Order order);

    List<OrderForProfileDto> toDto(List<Order> orders);

}
