package com.example.hop_oasis.service.data;

import com.example.hop_oasis.client.FedExRateClient;
import com.example.hop_oasis.convertor.FedExRateMapper;
import com.example.hop_oasis.convertor.OrderMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.enums.OrderStatus;
import com.example.hop_oasis.enums.PaymentStatus;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.*;
import com.example.hop_oasis.utils.*;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final EmailNotificationLogRepository emailLogRepository;
    private final PdfGenerator pdfGenerator;
    private final StripeService stripeService;
    private final TemplateService templateService;
    private final FedExRateMapper rateMapper;
    private final FedExRateClient rateClient;
    private final WeightCalculatorService weightCalculatorService;

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
        Map<String, String> names = fetchNamesForItems(cart.getCartItems());


        Order order = new Order();
        order.setUser(user);
        String orderCode = UniqueNumberGenerator.genStr();
        order.setOrderNumber(orderCode);
        order.setPaymentType(requestDto.getPaymentType());
        order.setCustomerPhoneNumber(requestDto.getCustomerPhoneNumber());
        validateAndSetDeliveryDetails(order, requestDto);
        List<CartItemDto> cartItemDtos = cart.getCartItems()
                .stream().map(cartService::createCartItemDto).toList();

        BigDecimal totalWeight = weightCalculatorService.calculateTotalWeight(cartItemDtos);
        order.setTotalWeight(totalWeight);
        FedExRateRequestDto rateRequest = rateMapper.toRateRequestDto(
                totalWeight.doubleValue(),
                requestDto.getDeliveryAddress()
        );
        BigDecimal deliveryCost = rateClient.getRate(rateRequest);
        order.setShippingPrice(deliveryCost);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PROCESSING);

        double totalPrice = 0.0;

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItemId(cartItem.getItemId());
            orderItem.setItemType(cartItem.getItemType());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setMeasureValue(cartItem.getMeasureValue());

            double pricePerItem = getPriceForCartItem(cartItem);
            orderItem.setPrice(pricePerItem);
            String key = cartItem.getItemId() + "_" + cartItem.getItemType().name();
            String itemTitle = names.getOrDefault(key, null);
            orderItem.setItemTitle(itemTitle);

            order.getOrderItems().add(orderItem);
            totalPrice += pricePerItem * cartItem.getQuantity();
        }
        order.setTotalPrice(Rounder.roundDoubleValue(totalPrice));
        orderRepository.save(order);
        cart.getCartItems().clear();
        cartRepository.save(cart);
        String orderDetails = templateService.buildOrderConfirmationEmailHtml(order);
        sendConfirmEmailWithoutInvoice(user.getEmail(),
                orderDetails);
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
            String invoiceHtml = templateService.buildInvoiceHtml(order);
            byte[] invoicePdf = pdfGenerator.generateFromHtml(invoiceHtml);
            emailService.sendEmail(user.getEmail(), EmailPattern.EMAIL_TITLE,
                    orderDetails, invoicePdf);

        } else {
            order = order.setPaymentStatus(PaymentStatus.NOT_PAID);
            String orderDetails = EmailPattern
                    .buildOrderNotPaidEmail(order, user.getFirstName(), user.getLastName());
            sendConfirmEmailWithoutInvoice(user.getEmail(),
                    orderDetails);

        }
        return orderMapper.toDto(order);

    }


    private void sendConfirmEmailWithoutInvoice(String email, String orderDetails) {
        emailService.sendEmail(email, EmailPattern.EMAIL_TITLE,
                orderDetails);
    }

    private Map<String, String> fetchNamesForItems(List<CartItem> cartItems) {
        Map<String, String> names = new HashMap<>();
        for (CartItem cartItem : cartItems) {
            String key = cartItem.getItemId() + "_" + cartItem.getItemType().name();
            switch (cartItem.getItemType()) {
                case BEER -> {
                    BeerInfoDto beerInfo = beerService.getBeerById(cartItem.getItemId());
                    names.put(key, beerInfo.getBeerName());
                }
                case CIDER -> {
                    CiderInfoDto ciderInfo = ciderService.getCiderById(cartItem.getItemId());
                    names.put(key, ciderInfo.getCiderName());
                }
                case SNACK -> {
                    SnackInfoDto snackInfo = snackService.getSnackById(cartItem.getItemId());
                    names.put(key, snackInfo.getSnackName());
                }
                case PRODUCT_BUNDLE -> {
                    ProductBundleInfoDto bundleInfo = bundleService.getProductBundleById(cartItem.getItemId());
                    names.put(key, bundleInfo.getName());
                }
            }
        }
        return names;
    }

    private double getPriceForCartItem(CartItem cartItem) {
        return switch (cartItem.getItemType()) {
            case BEER -> {
                BeerInfoDto beerInfo = beerService.getBeerById(cartItem.getItemId());
                if (beerInfo == null) {
                    throw new ResourceNotFoundException("Beer not found with id: " + cartItem.getItemId(), "");
                }
                if (cartItem.getMeasureValue() == null) {
                    throw new ResourceNotFoundException("Measure value is required for beer item", "");
                }
                BeerOptionsDto selectedVolume = cartService.chooseOptionByMeasureValue(
                        beerInfo.getOptions(), cartItem.getMeasureValue(), BeerOptionsDto::getVolume);
                yield selectedVolume.getPrice();
            }
            case CIDER -> {
                CiderInfoDto ciderInfo = ciderService.getCiderById(cartItem.getItemId());
                if (ciderInfo == null) {
                    throw new ResourceNotFoundException("Cider not found with id: " + cartItem.getItemId(), "");
                }
                if (cartItem.getMeasureValue() == null) {
                    throw new ResourceNotFoundException("Measure value is required for cider item", "");
                }
                CiderOptionsDto selectedVolume = cartService.chooseOptionByMeasureValue(
                        ciderInfo.getOptions(), cartItem.getMeasureValue(), CiderOptionsDto::getVolume);
                yield selectedVolume.getPrice();
            }
            case SNACK -> {
                SnackInfoDto snackInfo = snackService.getSnackById(cartItem.getItemId());
                if (snackInfo == null) {
                    throw new ResourceNotFoundException("Snack not found with id: " + cartItem.getItemId(), "");
                }
                if (cartItem.getMeasureValue() == null) {
                    throw new ResourceNotFoundException("Measure value is required for snack item", "");
                }
                SnackOptionsDto selectedWeight = cartService.chooseOptionByMeasureValue(
                        snackInfo.getOptions(), cartItem.getMeasureValue(), SnackOptionsDto::getWeight);
                yield selectedWeight.getPrice();
            }
            case PRODUCT_BUNDLE -> {
                ProductBundleInfoDto bundleInfo = bundleService.getProductBundleById(cartItem.getItemId());
                if (bundleInfo == null) {
                    throw new ResourceNotFoundException("Bundle not found with id: " + cartItem.getItemId(), "");
                }
                Optional<ProductBundleOptions> optionalProductBundleOptions =
                        productBundleOptionsRepository.findByProductBundleId(cartItem.getItemId());
                ProductBundleOptions options = optionalProductBundleOptions.orElseThrow(() ->
                        new ResourceNotFoundException("Bundle options not found", ""));
                yield options.getPrice();
            }
            default -> throw new ResourceNotFoundException("Unsupported item type: " + cartItem.getItemType(), "");

        };
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

    public List<OrderResponseDto> getAllOrdersForUser(Authentication authentication) {
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", ""));
        List<Order> orders = orderRepository.findByUserId(user.getId());
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for user with id: " + user.getId(), "");
        }
        List<OrderResponseDto> response = orderMapper.toDto(orders);
        response.forEach(orderDto ->
                orderDto.getItems().forEach(item -> item.setImageName(resolveItemImage(item))));
        return response;
    }

    private String resolveItemImage(OrderItemDto item) {
        try {
            return switch (item.getItemType()) {
                case BEER -> beerService.getBeerById(item.getItemId()).getImageName().stream().findFirst().orElse(null);
                case CIDER ->
                        ciderService.getCiderById(item.getItemId()).getCiderImageName().stream().findFirst().orElse(null);
                case SNACK ->
                        snackService.getSnackById(item.getItemId()).getSnackImageName().stream().findFirst().orElse(null);
                case PRODUCT_BUNDLE ->
                        bundleService.getProductBundleById(item.getItemId()).getProductImageName().stream().findFirst().orElse(null);
                default -> {
                    log.warn("Unknown item type {} for item {}", item.getItemType(), item.getItemId());
                    yield null;
                }
            };
        } catch (ResourceNotFoundException ex) {
            log.warn("Item not found: type={}, id={}", item.getItemType(), item.getItemId());
            return null;
        }
    }

    public List<OrderResponseDto> getAllOrdersByUserIdForAdmin(Long userId) {
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
    public OrderResponseDto updateUserOrderDetails(Long orderId, OrderRequestDto requestDto, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", ""));
        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND, ""));
        if (order.getOrderStatus() != OrderStatus.PROCESSING) {
            throw new IllegalArgumentException("Only orders with status PROCESSING can be updated.");
        }
        applyOrderUpdates(order, requestDto);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus, String cancellationReason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", ""));


        if (order.getOrderStatus() == newStatus) {
            throw new IllegalArgumentException("Order already has status " + newStatus);
        }

        order.setOrderStatus(newStatus);

        if (newStatus == OrderStatus.CANCELLED) {
            if (cancellationReason == null || cancellationReason.isBlank()) {
                throw new IllegalArgumentException("Cancellation reason is required when order is cancelled.");
            }
            order.setCancellationReason(cancellationReason);
        }

        Order updatedOrder = orderRepository.save(order);

        try {
            emailService.sendOrderStatusUpdateEmail(updatedOrder);
        } catch (Exception e) {
            log.error("Failed to send order status update email for order {}: {}", orderId, e.getMessage());
        }

        orderMapper.toDto(updatedOrder);
    }

    @Transactional
    public OrderResponseDto updateOrderDetailsByIdForAdmin(Long orderId, OrderRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", ""));
        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Order is cancelled, you can't change it");
        }
        applyOrderUpdates(order, requestDto);

        if (Objects.nonNull(requestDto.getOrderStatus())) {
            order.setOrderStatus(requestDto.getOrderStatus());
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

    public boolean resendOrderStatusEmail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: ", orderId));
        try {
            emailService.sendOrderStatusUpdateEmail(order);
            return true;
        } catch (Exception e) {
            log.error("Failed to resend order status email for order {}: {}", order.getId(), e.getMessage());
            return false;
        }
    }
}



