package com.example.hop_oasis.convertor;

import com.example.hop_oasis.dto.OrderItemDto;
import com.example.hop_oasis.dto.OrderResponseDto;
import com.example.hop_oasis.model.CartItem;
import com.example.hop_oasis.model.Order;
import com.example.hop_oasis.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderNumber", source = "orderNumber")
    @Mapping(target = "paymentType", source = "paymentType")
    @Mapping(target = "customerPhoneNumber", source = "customerPhoneNumber")
    @Mapping(target = "customerEmail", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "deliveryType", source = "deliveryType")
    @Mapping(target = "deliveryMethod", source = "deliveryMethod")
    @Mapping(target = "deliveryAddress", source = "deliveryAddress")
    @Mapping(target = "deliveryStatus", source = "deliveryStatus")
    OrderResponseDto toDto(Order order);

    @Mapping(target = "itemTitle", source = "orderItem.itemTitle")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "itemType", source = "cartItem.itemType")
    OrderItemDto toDto(CartItem cartItem);

    List<OrderResponseDto> toDto(List<Order> orders);
}
