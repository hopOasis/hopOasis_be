package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.OrderMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.enums.DeliveryStatus;
import com.example.hop_oasis.enums.PaymentStatus;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.*;
import com.example.hop_oasis.utils.EmailPattern;
import com.example.hop_oasis.utils.Rounder;
import com.example.hop_oasis.utils.UniqueNumberGenerator;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.*;

@Slf4j
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
    private final EmailService emailService;
    private final ProductBundleOptionsRepository productBundleOptionsRepository;
    private final StripeService stripeService;

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

    public String payForTheOrder(Long orderId) throws StripeException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found", ""));
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new ResourceNotFoundException("Order is paid", "");
        }
        return stripeService.createPaymentLink(order.getTotalPrice(), order.getOrderNumber());
    }

    public OrderResponseDto isOrderPaid(boolean success, Authentication authentication, Long orderId) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found", ""));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found", ""));
        if (success) {
            order.setPaymentStatus(PaymentStatus.PAID);
            order = orderRepository.save(order);
            String orderDetails = EmailPattern
                    .buildOrderConfirmationEmail(order, user.getFirstName(), user.getLastName());
            sendConfirmEmail(user.getEmail(),
                    orderDetails);

        } else {
            order = order.setPaymentStatus(PaymentStatus.NOT_PAID);
            String orderDetails = EmailPattern
                    .buildOrderNotPaidEmail(order, user.getFirstName(), user.getLastName());
            sendConfirmEmail(user.getEmail(),
                    orderDetails);

        }
        return orderMapper.toDto(order);

    }


    private void sendConfirmEmail(String email, String orderDetails) {
        emailService.sendEmail(email, EmailPattern.EMAIL_TITLE,
                orderDetails);
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

    @Transactional
    public OrderResponseDto updateUserOrder(Long orderId, OrderRequestDto requestDto, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", ""));
        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND, ""));
        if (order.getDeliveryStatus() != DeliveryStatus.PROCESSING) {
            throw new IllegalArgumentException("Only orders with status PROCESSING can be updated.");
        }
        applyOrderUpdates(order, requestDto);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Transactional
    public OrderResponseDto updateOrderByIdForAdmin(Long orderId, OrderRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", ""));
        applyOrderUpdates(order, requestDto);
        if (Objects.nonNull(requestDto.getDeliveryStatus())) {
            order.setDeliveryStatus(requestDto.getDeliveryStatus());
        }

        return orderMapper.toDto(orderRepository.save(order));
    }

    private void applyOrderUpdates(Order order, OrderRequestDto requestDto) {
        if (Objects.nonNull(requestDto.getCustomerPhoneNumber())) {
            order.setCustomerPhoneNumber(requestDto.getCustomerPhoneNumber());
        }
        if (Objects.nonNull(requestDto.getPaymentType())) {
            order.setPaymentType(requestDto.getPaymentType());
        }
        if (Objects.nonNull(requestDto.getDeliveryMethod())) {
            order.setDeliveryMethod(requestDto.getDeliveryMethod());
        }
        if (Objects.nonNull(requestDto.getDeliveryAddress())) {
            order.setDeliveryAddress(requestDto.getDeliveryAddress());
        }
    }


    public void deleteOrderByIdForAdmin(Long orderId) {
        orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ORDER_DELETED, orderId));
        orderRepository.deleteById(orderId);
    }

    public void deleteUserOrder(Long orderId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", ""));
        orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ORDER_DELETED, orderId));
        orderRepository.deleteById(orderId);
    }

    private void validateAndSetDeliveryDetails(Order order, OrderRequestDto requestDto) {
        if (requestDto.getDeliveryMethod() == null) {
            throw new ResourceNotFoundException("Delivery method cannot be null or empty", "");

        }
        order.setDeliveryMethod(requestDto.getDeliveryMethod());

        if (requestDto.getDeliveryAddress() == null || requestDto.getDeliveryAddress().isEmpty()) {
            throw new ResourceNotFoundException("Delivery address cannot be null or empty", "");
        }
        order.setDeliveryAddress(requestDto.getDeliveryAddress());


    }
}



