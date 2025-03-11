package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.OrderRequestDto;
import com.example.hop_oasis.dto.OrderResponseDto;
import com.example.hop_oasis.service.data.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid
                                                        @RequestBody OrderRequestDto requestDto,
                                                        Authentication authentication) {
        return ResponseEntity.ok(orderService.createOrder(requestDto, authentication));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> responseDtos = orderService.getAllOrders();
        return ResponseEntity.ok().body(responseDtos);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        OrderResponseDto responseDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getAllOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDto> responseDtos = orderService.getAllOrdersByUserId(userId);
        return ResponseEntity.ok().body(responseDtos);

    }

    @PutMapping("/user/{orderId}")
    public ResponseEntity<OrderResponseDto> updateUserOrder(@PathVariable Long orderId,
                                                            @RequestBody OrderRequestDto requestDto,
                                                            Authentication authentication) {
        OrderResponseDto responseDto = orderService.updateUserOrder(orderId, requestDto, authentication);
        return ResponseEntity.ok().body(responseDto);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderForAdmin(@PathVariable Long orderId,
                                                                @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.updateOrderByIdForAdmin(orderId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrderByIdForAdmin(@PathVariable Long orderId) {
        orderService.deleteOrderByIdForAdmin(orderId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{orderId}")
    public ResponseEntity<Void> deleteUserOrder(@PathVariable Long orderId,
                                                Authentication authentication) {
        orderService.deleteUserOrder(orderId, authentication);
        return ResponseEntity.noContent().build();
    }
}
