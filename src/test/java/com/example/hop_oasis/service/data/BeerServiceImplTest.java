package com.example.hop_oasis.service.data;

import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.ImageRepository;
import com.example.hop_oasis.convertor.BeerMapper;
import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.convertor.ImageMapper;
import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.dto.ImageDto;


import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.springframework.web.multipart.MultipartFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.BEERS_NOT_FOUND;
import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.BEER_NOT_FOUND;
import static org.mockito.Mockito.*;

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {
    private final static Long ID = 1L;
    @InjectMocks
    private BeerServiceImpl beerServiceImpl;
    @Mock
    private ImageCompressor imageCompressor;
    @Mock
    private BeerRepository beerRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ImageMapper imageMapper;
    @Mock
    private BeerMapper beerMapper;
    @Mock
    private BeerInfoMapper beerInfoMapper;
    @Mock
    private Beer beer;
    private BeerInfoDto beerInfoDto;
    private BeerDto beerDto;
    @BeforeEach
    void setUp() {
        beerDto = new BeerDto();
        beerInfoDto = new BeerInfoDto();
    }
    @Test
    void shouldSaveObject() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        byte[] compressedImageData = "Test compressed image data".getBytes();
        byte[] originalImageData = "Test original image data".getBytes();

        when(multipartFile.getBytes()).thenReturn(originalImageData);
        when(imageCompressor.compressImage(originalImageData)).thenReturn(compressedImageData);
        when(imageMapper.toDto(any())).thenReturn(new ImageDto());

        beerServiceImpl.save(multipartFile, beerDto);

        verify(multipartFile, times(1)).getBytes();
        verify(imageCompressor, times(1)).compressImage(originalImageData);
        verify(beerMapper, times(1)).toEntity(beerDto);
        verify(beerRepository, times(1)).save(any());
        verify(imageMapper, times(1)).toDto(any());
        verify(imageRepository, times(1)).save(any());
    }
    @Test
    void shouldReturnBeerById() {
        when(beerRepository.findById(ID)).thenReturn(Optional.of(beer));
        when(beerInfoMapper.toDto(beer)).thenReturn(beerInfoDto);

        BeerInfoDto result = beerServiceImpl.getBeerById(ID);

        assertNotNull(result);
        assertSame(beerInfoDto, result);
        verify(beerRepository, times(1)).findById(ID);
        verify(beerInfoMapper, times(1)).toDto(beer);
    }
    @Test
    void shouldThrowBeerNotFoundException() {
        when(beerRepository.findById(ID)).thenReturn(Optional.empty());

        BeerNotFoundException exception = assertThrows(BeerNotFoundException.class, () -> {
            beerServiceImpl.getBeerById(ID);
        });

        String expectedMessage = String.format(BEER_NOT_FOUND, ID);
        assertEquals(expectedMessage, exception.getMessage());
        verify(beerRepository).findById(ID);
        verify(beerInfoMapper, never()).toDto(any(Beer.class));
    }
    @Test
    void shouldReturnAllBeers() {
        List<Beer> beers = List.of(beer, beer);
        List<BeerInfoDto> beerInfoDtos = List.of(beerInfoDto, beerInfoDto);

        when(beerRepository.findAll()).thenReturn(beers);
        when(beerInfoMapper.toDtos(beers)).thenReturn(beerInfoDtos);

        List<BeerInfoDto> result = beerServiceImpl.getAllBeers();

        assertNotNull(result);
        assertEquals(beerInfoDtos.size(), result.size());
        verify(beerRepository).findAll();
        verify(beerInfoMapper).toDtos(beers);
    }
    @Test
    void shouldReturnBeerNotFoundException() {
        when(beerRepository.findAll()).thenReturn(Collections.emptyList());

        BeerNotFoundException exception = assertThrows(BeerNotFoundException.class, () -> {
            beerServiceImpl
                    .getAllBeers();
        });
        String expectedMessage = String.format(BEERS_NOT_FOUND, "");
        assertEquals(expectedMessage, exception.getMessage());
        verify(beerRepository).findAll();
        verify(beerInfoMapper, never()).toDtos(anyList());
    }
    @Test
    void shouldUpdateObject() {
        beerInfoDto.setBeerName("UpdatedBeer");
        beerInfoDto.setVolumeLarge(500.0);
        beerInfoDto.setVolumeSmall(300.0);
        beerInfoDto.setPriceLarge(10.0);
        beerInfoDto.setPriceSmall(7.0);
        beerInfoDto.setDescription("Updated description");
        beerInfoDto.setBearColor("Updated color");

        beer = new Beer();
        beer.setId(ID);
        when(beerRepository.findById(ID)).thenReturn(Optional.of(beer));

        beerServiceImpl.update(beerInfoDto, ID);

        verify(beerRepository, times(1)).findById(ID);
        verify(beerRepository, times(1)).save(beer);
        assertEquals("UpdatedBeer", beer.getBeerName());
        assertEquals(500.0, beer.getVolumeLarge());
        assertEquals(300.0, beer.getVolumeSmall());
        assertEquals(10.0, beer.getPriceLarge());
        assertEquals(7.0, beer.getPriceSmall());
        assertEquals("Updated description", beer.getDescription());
        assertEquals("Updated color", beer.getBearColor());
    }
    @Test
    void shouldThrowException() {
        when(beerRepository.findById(ID)).thenReturn(Optional.empty());

        BeerNotFoundException exception = assertThrows(BeerNotFoundException.class, () -> {
            beerServiceImpl.update(beerInfoDto, ID);
        });

        String expectedMessage = String.format(BEER_NOT_FOUND, ID);
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