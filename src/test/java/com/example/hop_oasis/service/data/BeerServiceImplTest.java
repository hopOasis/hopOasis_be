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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {
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
    @InjectMocks
    private BeerServiceImpl beerServiceImpl;

    @Test
    void save() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        BeerDto beerDto = new BeerDto();
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
    void getBeerById() {
        Long beerId = 2L;
        Beer beer = new Beer();
        when(beerRepository.findById(beerId)).thenReturn(Optional.of(beer));
        BeerInfoDto beerInfoDto = new BeerInfoDto();
        when(beerInfoMapper.toDto(beer)).thenReturn(beerInfoDto);

        BeerInfoDto result = beerServiceImpl.getBeerById(beerId);

        assertNotNull(result);
        assertSame(beerInfoDto, result);
        verify(beerRepository, times(1)).findById(beerId);
        verify(beerInfoMapper, times(1)).toDto(beer);
    }

    @Test
    void beerNotFoundException() {
        Long beerId = 1L;
        when(beerRepository.findById(beerId)).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> beerServiceImpl.getBeerById(beerId), "Expected BeerNotFoundException to be thrown");
        verify(beerRepository, times(1)).findById(beerId);
        verifyNoInteractions(beerInfoMapper);

    }

    @Test
    void getAllBeers() {
        List<Beer> beers = new ArrayList<>();
        when(beerRepository.findAll()).thenReturn(beers);

        assertThrows(BeerNotFoundException.class, () -> beerServiceImpl.getAllBeers(), "Expected BeerNotFoundException to be thrown");
        verify(beerRepository, times(1)).findAll();
        verifyNoInteractions(beerInfoMapper);

    }

    @Test
    void update() {
        Long id = 1L;
        BeerInfoDto beerInfo = new BeerInfoDto();
        beerInfo.setBeerName("UpdatedBeer");
        beerInfo.setVolumeLarge(500.0);
        beerInfo.setVolumeSmall(300.0);
        beerInfo.setPriceLarge(10.0);
        beerInfo.setPriceSmall(7.0);
        beerInfo.setDescription("Updated description");
        beerInfo.setBearColor("Updated color");
        Beer beer = new Beer();
        beer.setId(id);
        when(beerRepository.findById(id)).thenReturn(Optional.of(beer));

        beerServiceImpl.update(beerInfo, id);

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
    void beerNotUpdated() {
        Long beerId = 3L;
        Beer beer = new Beer();
        beer.setId(beerId);
        BeerInfoDto beerInfoDto = new BeerInfoDto();
        when(beerRepository.findById(beerId)).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> beerServiceImpl.update(beerInfoDto, beerId), "Expected BeerNotFoundException to be thrown");
        verify(beerRepository, never()).save(any());
        verifyNoInteractions(beerInfoMapper);
    }

    @Test
    void delete() {
        Long beerId = 1L;
        Beer beer = new Beer();
        beer.setId(beerId);
        when(beerRepository.findById(beerId)).thenReturn(Optional.of(beer));

        beerServiceImpl.delete(beerId);

        verify(beerRepository).findById(beerId);
        verify(beerRepository).deleteById(beerId);
    }

}