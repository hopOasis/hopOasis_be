package com.example.hop_oasis.service.data;

import com.example.hop_oasis.component.Cart;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.model.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    private final static Long ID = 1L;
    private static int quantity = 7;
    @InjectMocks
    private CartServiceImpl cartService;
    @Mock
    private Cart cart;
    @Mock
    private BeerServiceImpl beerService;
    @Mock
    private ProductBundleServiceImpl bundleService;
    @Mock
    private SnackServiceImpl snackService;
    @Mock
    private CiderServiceImpl ciderService;
    @Mock
    private BeerInfoDto beerInfoDto;
    private SnackInfoDto snackInfoDto;
    private CiderInfoDto ciderInfoDto;
    private ProductBundleInfoDto productBundleInfoDto;

    @BeforeEach
    void setUp() {
        reset(cart, beerService, bundleService, snackService, ciderService);
        beerInfoDto = new BeerInfoDto();
        snackInfoDto = new SnackInfoDto();
        ciderInfoDto = new CiderInfoDto();
        productBundleInfoDto = new ProductBundleInfoDto();
    }

    @Test
    void shouldReturnAllItemsInCart() {
        Map<Long, Integer> beers = new HashMap<>();
        beers.put(ID, 1);
        Map<Long, Integer> snacks = new HashMap<>();
        snacks.put(ID, 1);
        Map<Long, Integer> products = new HashMap<>();
        products.put(ID, 1);
        Map<Long, Integer> cider = new HashMap<>();
        cider.put(ID, 1);

        beerInfoDto.setBeerName("Beer");
        beerInfoDto.setPriceLarge(1);

        snackInfoDto.setSnackName("Chips");
        snackInfoDto.setPriceSmall(3);

        ciderInfoDto.setCiderName("Apple");
        ciderInfoDto.setPriceLarge(1);

        productBundleInfoDto.setName("Fish");
        productBundleInfoDto.setPrice(1);

        when(cart.getBeers()).thenReturn(beers);
        when(cart.getSnacks()).thenReturn(snacks);
        when(cart.getProductBundles()).thenReturn(products);
        when(cart.getCider()).thenReturn(cider);

        when(beerService.getBeerById(ID)).thenReturn(beerInfoDto);
        when(snackService.getSnackById(ID)).thenReturn(snackInfoDto);
        when(ciderService.getCiderById(ID)).thenReturn(ciderInfoDto);
        when(bundleService.getProductBundleById(ID)).thenReturn(productBundleInfoDto);

        CartDto cartDto = cartService.getAllItems();

        assertEquals(4, cartDto.getItems().size());
        assertEquals(6, cartDto.getPriceForAll());


    }

    @Test
    void shouldAddNewItem() {
        Long beerId = 1L;
        Long snackId = 2L;
        Long ciderId = 3L;
        Long bundleId = 4L;
        beerInfoDto.setBeerName("Beer");
        snackInfoDto.setSnackName("Chips");
        ciderInfoDto.setCiderName("Apple");
        productBundleInfoDto.setName("Bundle");

        when(beerService.getBeerById(beerId)).thenReturn(beerInfoDto);
        when(snackService.getSnackById(snackId)).thenReturn(snackInfoDto);
        when(ciderService.getCiderById(ciderId)).thenReturn(ciderInfoDto);
        when(bundleService.getProductBundleById(bundleId)).thenReturn(productBundleInfoDto);

        cartService.add(beerId, ItemType.BEER);
        cartService.add(snackId, ItemType.SNACK);
        cartService.add(ciderId, ItemType.CIDER);
        cartService.add(bundleId, ItemType.PRODUCT_BUNDLE);

        verify(cart).add(beerId, ItemType.BEER);
        verify(cart).add(snackId, ItemType.SNACK);
        verify(cart).add(ciderId, ItemType.CIDER);
        verify(cart).add(bundleId, ItemType.PRODUCT_BUNDLE);

    }

    @Test
    void shouldUpdateItem() {
        cartService.updateQuantity(ID, quantity, ItemType.BEER);
        verify(cart).updateQuantity(ID, quantity, ItemType.BEER);
        cartService.updateQuantity(ID, quantity, ItemType.CIDER);
        verify(cart).updateQuantity(ID, quantity, ItemType.CIDER);

    }

    @Test
    void shouldRemoveItem() {
        cartService.removeItem(ID, ItemType.BEER);
        cartService.removeItem(ID, ItemType.CIDER);
        verify(cart).remove(ID, ItemType.BEER);
        verify(cart).remove(ID, ItemType.CIDER);
    }

    @Test
    void shouldDeleteCart() {
        cartService.delete();
        verify(cart).delete();


    }

}