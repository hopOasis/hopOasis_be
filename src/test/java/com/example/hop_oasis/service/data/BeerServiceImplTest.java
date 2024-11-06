package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.convertor.BeerMapper;
import com.example.hop_oasis.convertor.BeerOptionsMapper;
import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.ItemRatingDto;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.repository.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {
    private final static Long ID = 1L;


    @Mock
    private BeerRepository beerRepository;

    @Mock
    private BeerMapper beerMapper;

    @Mock
    private BeerInfoMapper beerInfoMapper;

    @Mock
    private BeerOptionsMapper beerOptionsMapper;

    @Mock
    private BeerRatingServiceImpl beerRatingService;

    @InjectMocks
    private BeerServiceImpl beerServiceImpl;

    @Mock
    private Beer beer;

    private BeerInfoDto beerInfoDto;
    private ItemRatingDto itemRatingDto;
    private BeerDto beerDto;

    @BeforeEach
    void setUp() {
        beerDto = new BeerDto();
        beerInfoDto = new BeerInfoDto();
        itemRatingDto = new ItemRatingDto(ID, 0, 0);
    }

    @Test
    void shouldSaveObject() {

        when(beerMapper.toEntity(any(BeerDto.class))).thenReturn(beer);

        beerServiceImpl.save(beerDto);

        verify(beerMapper, times(1)).toEntity(beerDto);
        verify(beerRepository, times(1)).save(any());
        verify(beerOptionsMapper, times(1)).toEntities(any());
    }

    @Test
    void shouldReturnBeerById() {
        when(beerRepository.findById(ID)).thenReturn(Optional.of(beer));
        when(beerInfoMapper.toDto(beer)).thenReturn(beerInfoDto);
        when(beerRatingService.getItemRating(anyLong())).thenReturn(itemRatingDto);

        BeerInfoDto result = beerServiceImpl.getBeerById(ID);

        assertNotNull(result);
        assertSame(beerInfoDto, result);
        verify(beerRepository, times(1)).findById(ID);
        verify(beerInfoMapper, times(1)).toDto(beer);
        verify(beerRatingService, times(1)).getItemRating(anyLong());
    }

    @Test
    void shouldThrowBeerNotFoundException() {
        when(beerRepository.findById(ID)).thenReturn(Optional.empty());

        final var exception = assertThrows(ResourceNotFoundException.class,
                () -> beerServiceImpl.getBeerById(ID));

        String expectedMessage = String.format(RESOURCE_NOT_FOUND, ID);
        assertEquals(expectedMessage, exception.getMessage());
        verify(beerRepository).findById(ID);
        verify(beerInfoMapper, never()).toDto(any(Beer.class));
    }

    @Test
    void shouldReturnAllBeers() {
        final var beers = List.of(beer, beer);
        final var beerPage = new PageImpl<>(beers);

        final var pageable = Pageable.unpaged();
        when(beerRepository.findAll(pageable)).thenReturn(beerPage);
        when(beerInfoMapper.toDto(beer)).thenReturn(beerInfoDto);
        when(beerRatingService.getItemRating(anyLong())).thenReturn(itemRatingDto);

        final var result = beerServiceImpl.getAllBeers(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        verify(beerRepository).findAll(pageable);
        verify(beerInfoMapper, times(2)).toDto(any(Beer.class));
        verify(beerRatingService, times(2)).getItemRating(anyLong());
    }

    @Test
    void shouldReturnEmptyPage() {
        final var emptyBeerPage = new PageImpl<Beer>(List.of());

        // Создаем объект Pageable
        final var pageable = PageRequest.of(0, 10);

        // Мокируем вызов findAll(pageable) с пустой страницей
        when(beerRepository.findAll(pageable)).thenReturn(emptyBeerPage);

        final var result = beerServiceImpl.getAllBeers(pageable);
        assertTrue(result.isEmpty());

        verify(beerRepository).findAll(pageable);
        verify(beerInfoMapper, never()).toDtos(anyList());
    }

    @Test
    void shouldUpdateObject() {
        beerDto.setBeerName("UpdatedBeer");
        beerDto.setDescription("Updated description");
        beerDto.setBeerColor("Updated color");
        beerDto.setOptions(List.of());

        beer = new Beer();
        beer.setId(ID);
        beer.setBeerOptions(new ArrayList<>());

        when(beerRepository.findById(ID)).thenReturn(Optional.of(beer));
        when(beerRepository.save(beer)).thenReturn(beer);
        when(beerOptionsMapper.toEntities(any())).thenReturn(List.of());

        beerServiceImpl.update(beerDto, ID);

        verify(beerRepository, times(1)).findById(ID);
        verify(beerRepository, times(1)).save(beer);
        verify(beerInfoMapper, times(1)).toDto(any(Beer.class));

        assertEquals("UpdatedBeer", beerDto.getBeerName());
        assertEquals("Updated description", beerDto.getDescription());
        assertEquals("Updated color", beerDto.getBeerColor());
    }

    @Test
    void shouldThrowException() {
        when(beerRepository.findById(ID)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                beerServiceImpl.update(beerDto, ID));

        String expectedMessage = String.format(RESOURCE_NOT_FOUND, ID);
        assertEquals(expectedMessage, exception.getMessage());
        verify(beerRepository, never()).save(any(Beer.class));
    }

    @Test
    void shouldDeleteObjectById() {
        beer.setId(ID);
        when(beerRepository.findById(ID)).thenReturn(Optional.of(beer));

        beerServiceImpl.delete(ID);

        verify(beerRepository).findById(ID);
        verify(beerRepository).deleteById(ID);
    }
}