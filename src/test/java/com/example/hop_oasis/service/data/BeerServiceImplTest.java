package com.example.hop_oasis.service.data;

import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.hendler.exception.BeerNotFoundException;
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

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {
    private static Long id = 1L;

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

        when(beerRepository.findById(id)).thenReturn(Optional.of(beer));
        when(beerInfoMapper.toDto(beer)).thenReturn(beerInfoDto);

        BeerInfoDto result = beerServiceImpl.getBeerById(id);

        assertNotNull(result);
        assertSame(beerInfoDto, result);
        verify(beerRepository, times(1)).findById(id);
        verify(beerInfoMapper, times(1)).toDto(beer);
    }

    @Test
    void shouldThrowBeerNotFoundException() {
        when(beerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> beerServiceImpl.getBeerById(id),
                "Expected BeerNotFoundException to be thrown");
        verify(beerRepository, times(1)).findById(id);
        verifyNoInteractions(beerInfoMapper);

    }

    @Test
    void shouldReturnAllBeers() {
        List<Beer> beers = new ArrayList<>();
        when(beerRepository.findAll()).thenReturn(beers);

        assertThrows(BeerNotFoundException.class, () -> beerServiceImpl.getAllBeers(),
                "Expected BeerNotFoundException to be thrown");
        verify(beerRepository, times(1)).findAll();
        verifyNoInteractions(beerInfoMapper);

    }

    @BeforeEach
    void init() {
        beer = new Beer();
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
        beer.setId(id);
        when(beerRepository.findById(id)).thenReturn(Optional.of(beer));

        beerServiceImpl.update(beerInfoDto, id);

        verify(beerRepository, times(1)).findById(id);
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
        beer.setId(id);
        when(beerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> beerServiceImpl.update(beerInfoDto, id),
                "Expected BeerNotFoundException to be thrown");
        verify(beerRepository, never()).save(any());
        verifyNoInteractions(beerInfoMapper);
    }

    @Test
    void shouldDeleteObjectById() {
        beer.setId(id);
        when(beerRepository.findById(id)).thenReturn(Optional.of(beer));

        beerServiceImpl.delete(id);

        verify(beerRepository).findById(id);
        verify(beerRepository).deleteById(id);
    }

}