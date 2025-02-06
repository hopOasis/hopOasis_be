package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.OrderMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.enums.DeliveryStatus;
import com.example.hop_oasis.enums.DeliveryType;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.*;
import com.example.hop_oasis.utils.Rounder;
import com.example.hop_oasis.utils.UniqueNumberGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_DELETED;
import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final BeerServiceImpl beerService;
    private final CartServiceImpl cartService;
    private final CiderServiceImpl ciderService;
    private final SnackServiceImpl snackService;
    private final ProductBundleServiceImpl bundleService;
    private final AuthenticationService authenticationService;
    private final ProductBundleOptionsRepository productBundleOptionsRepository;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, Authentication authentication) {
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", ""));
        Cart cart = cartRepository.findByUserId(user.getId()).
                orElseThrow(() -> new ResourceNotFoundException("Cart not found", ""));
        if (cart.getCartItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty", "");
        }
        Map<Long, Double> prices = fetchPricesForItems(cart.getCartItems());
        Map<Long, String> names = fetchNamesForItems(cart.getCartItems());


        Order order = new Order();
        order.setUser(user);
        String orderCode = UniqueNumberGenerator.genStr();
        order.setOrderNumber(orderCode);
        order.setPaymentType(requestDto.getPaymentType());
        order.setCustomerPhoneNumber(requestDto.getCustomerPhoneNumber());
        order.setDeliveryType(requestDto.getDeliveryType());
        validateAndSetDeliveryDetails(order, requestDto);
        order.setCreatedAt(LocalDateTime.now());
        order.setDeliveryStatus(DeliveryStatus.PROCESSING);

        double totalPrice = 0.0;

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItemId(cartItem.getItemId());
            orderItem.setItemType(cartItem.getItemType());
            orderItem.setQuantity(cartItem.getQuantity());

            double pricePerItem = prices.getOrDefault(cartItem.getItemId(), 0.0);
            orderItem.setPrice(pricePerItem);
            String itemTitle = names.getOrDefault(cartItem.getItemId(), null);
            orderItem.setItemTitle(itemTitle);

            order.getOrderItems().add(orderItem);
            totalPrice += pricePerItem * cartItem.getQuantity();
        }
        order.setTotalPrice(Rounder.roundDoubleValue(totalPrice));
        orderRepository.save(order);
        cart.getCartItems().clear();
        cartRepository.save(cart);
        return orderMapper.toDto(order);
    }

    private Map<Long, String> fetchNamesForItems(List<CartItem> cartItems) {
        Map<Long, String> names = new HashMap<>();
        for (CartItem cartItem : cartItems) {
            switch (cartItem.getItemType()) {
                case BEER -> {
                    BeerInfoDto beerInfo = beerService.getBeerById(cartItem.getItemId());
                    names.put(cartItem.getItemId(), beerInfo.getBeerName());
                }
                case CIDER -> {
                    CiderInfoDto ciderInfo = ciderService.getCiderById(cartItem.getItemId());
                    names.put(cartItem.getItemId(), ciderInfo.getCiderName());
                }
                case SNACK -> {
                    SnackInfoDto snackInfo = snackService.getSnackById(cartItem.getItemId());
                    names.put(cartItem.getItemId(), snackInfo.getSnackName());
                }
                case PRODUCT_BUNDLE -> {
                    ProductBundleInfoDto bundleInfo = bundleService.getProductBundleById(cartItem.getItemId());
                    names.put(cartItem.getItemId(), bundleInfo.getName());
                }
            }
        }
        return names;
    }

    private Map<Long, Double> fetchPricesForItems(List<CartItem> cartItems) {
        Map<Long, Double> prices = new HashMap<>();
        for (CartItem cartItem : cartItems) {
            switch (cartItem.getItemType()) {
                case BEER -> {
                    BeerInfoDto beerInfo = beerService.getBeerById(cartItem.getItemId());
                    if (beerInfo != null && cartItem.getMeasureValue() != null) {
                        BeerOptionsDto selectedVolume = cartService.chooseOptionByMeasureValue(
                                beerInfo.getOptions(), cartItem.getMeasureValue(), BeerOptionsDto::getVolume);
                        prices.put(cartItem.getItemId(), selectedVolume.getPrice());

                    }
                }
                case CIDER -> {
                    CiderInfoDto ciderInfo = ciderService.getCiderById(cartItem.getItemId());
                    if (ciderInfo != null && cartItem.getMeasureValue() != null) {
                        CiderOptionsDto selectedVolume = cartService.chooseOptionByMeasureValue(
                                ciderInfo.getOptions(), cartItem.getMeasureValue(), CiderOptionsDto::getVolume);
                        prices.put(cartItem.getItemId(), selectedVolume.getPrice());

                    }
                }
                case SNACK -> {
                    SnackInfoDto snackInfo = snackService.getSnackById(cartItem.getItemId());
                    if (snackInfo != null && cartItem.getMeasureValue() != null) {
                        SnackOptionsDto selectedWeight = cartService.chooseOptionByMeasureValue(
                                snackInfo.getOptions(), cartItem.getMeasureValue(), SnackOptionsDto::getWeight);
                        prices.put(cartItem.getItemId(), selectedWeight.getPrice());
                    }
                }
                case PRODUCT_BUNDLE -> {
                    ProductBundleInfoDto bundleInfo = bundleService.getProductBundleById(cartItem.getItemId());
                    if (bundleInfo != null) {
                        Optional<ProductBundleOptions> optionalProductBundleOptions = productBundleOptionsRepository
                                .findByProductBundleId(cartItem.getItemId());
                        ProductBundleOptions options = optionalProductBundleOptions
                                .orElseThrow(() -> new ResourceNotFoundException("Bundle options not found", ""));
                        prices.put(cartItem.getItemId(), options.getPrice());
                    }
                }
                default -> throw new ResourceNotFoundException("Unsupported item type: " + cartItem.getItemType(), "");

            }
        }
        return prices;
    }

    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("Order list is empty", "");
        } else return orderMapper.toDto(orders);
    }

    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", ""));
        return orderMapper.toDto(order);
    }

    public List<OrderResponseDto> getAllOrdersByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found", "");
        }
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for user with id: " + userId, "");
        }
        return orderMapper.toDto(orders);
    }

    public OrderResponseDto updateOrderById(OrderRequestDto requestDto, Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND, ""));
        boolean isAdmin = authenticationService.isAdmin();

        if (isAdmin || order.getDeliveryStatus() == DeliveryStatus.PROCESSING) {
            if (Objects.nonNull(requestDto.getCustomerPhoneNumber())) {
                order.setCustomerPhoneNumber(requestDto.getCustomerPhoneNumber());
            }
            if (Objects.nonNull(requestDto.getDeliveryType())) {
                validateAndSetDeliveryDetails(order, requestDto);
            }

        } else {
            throw new ResourceNotFoundException("Only orders with status ACCEPTED can be updated.", "");
        }

        if (Objects.nonNull(requestDto.getDeliveryStatus())) {
            if (isAdmin) {
                order.setDeliveryStatus(requestDto.getDeliveryStatus());
            } else {
                throw new ResourceNotFoundException("Only admin can change status", "");
            }
        }

        return orderMapper.toDto(orderRepository.save(order));

    }

    public OrderResponseDto deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_DELETED, id));
        orderRepository.deleteById(id);
        return orderMapper.toDto(order);
    }

    private void validateAndSetDeliveryDetails(Order order, OrderRequestDto requestDto) {
        order.setDeliveryType(requestDto.getDeliveryType());

        if (requestDto.getDeliveryType() == DeliveryType.DELIVERY) {
            if (requestDto.getDeliveryMethod() == null) {
                throw new ResourceNotFoundException("Delivery method cannot be null or empty", "");
            }
            if (requestDto.getDeliveryAddress() == null || requestDto.getDeliveryAddress().isEmpty()) {
                throw new ResourceNotFoundException("Delivery address cannot be null or empty", "");
            }

            order.setDeliveryMethod(requestDto.getDeliveryMethod());
            order.setDeliveryAddress(requestDto.getDeliveryAddress());
        } else if (requestDto.getDeliveryType() == DeliveryType.PICKUP) {
            if (Objects.nonNull(requestDto.getDeliveryMethod())) {
                throw new ResourceNotFoundException("No need delivery method for pickup", "");
            }
            if (Objects.nonNull(requestDto.getDeliveryAddress()) && !requestDto.getDeliveryAddress().isEmpty()) {
                throw new ResourceNotFoundException("No need address for pickup", "");
            }

            order.setDeliveryMethod(null);
            order.setDeliveryAddress(null);
        }
    }

}

