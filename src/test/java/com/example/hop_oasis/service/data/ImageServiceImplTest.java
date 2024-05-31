package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ImageMapper;
import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.ImageDto;
import com.example.hop_oasis.hendler.exception.BeerNotFoundException;
import com.example.hop_oasis.hendler.exception.ImageNotFoundException;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.Image;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {
    private static final String IMAGE_NAME = "image.jpg";
    private static final Long ID = 1L;
    private static final Long BEER_ID = 1L;
    private static final byte[] COMPRESSED_IMAGE_DATA = new byte[]{/* Compressed data*/};
    private static final byte[] DECOMPRESSED_IMAGE_DATA = new byte[]{/* Decompressed data*/};
    @InjectMocks
    private ImageServiceImpl imageService;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ImageCompressor imageCompressor;
    @Mock
    private ImageMapper imageMapper;
    @Mock
    private BeerRepository beerRepository;
    @Mock
    private Beer beer;
    @Mock
    private Image image;
    @Mock
    private MockMultipartFile multipartFile;
    private ImageDto imageDto;
    @BeforeEach
    void init() {
        image = new Image();
        image.setName(IMAGE_NAME);
        image.setImage(COMPRESSED_IMAGE_DATA);

        imageDto = new ImageDto();}
    @Test
    void shouldReturnImageByName() {
        when(imageRepository.findByName(IMAGE_NAME)).thenReturn(Optional.of(image));
        when(imageCompressor.decompressImage(COMPRESSED_IMAGE_DATA, IMAGE_NAME))
                .thenReturn(DECOMPRESSED_IMAGE_DATA);
        when(imageMapper.toDto(image)).thenReturn(imageDto);

        ImageDto result = imageService.getImageByName(IMAGE_NAME);

        assertNotNull(result);
        assertEquals(imageDto, result);
        verify(imageRepository).findByName(IMAGE_NAME);
        verify(imageCompressor).decompressImage(COMPRESSED_IMAGE_DATA, IMAGE_NAME);
        assertArrayEquals(DECOMPRESSED_IMAGE_DATA, image.getImage());
        verify(imageMapper).toDto(image);
    }
    @Test
    void shouldThrowImageFoundException() {
        when(imageRepository.findByName(IMAGE_NAME)).thenReturn(Optional.empty());

        ImageNotFoundException exception = assertThrows(ImageNotFoundException.class, () -> {
            imageService.getImageByName(IMAGE_NAME);
        });

        String expectedMessage = String.format(IMAGE_NOT_FOUND, IMAGE_NAME);
        assertEquals(expectedMessage, exception.getMessage());
        verify(imageRepository).findByName(IMAGE_NAME);
        verify(imageCompressor, never()).decompressImage(any(byte[].class), eq(IMAGE_NAME));
        verify(imageMapper, never()).toDto(any(Image.class));
    }
    @Test
    void shouldAddImageToBeer() throws IOException {
       MultipartFile multipart = new MockMultipartFile(
                "file",
                IMAGE_NAME,
                "image/jpeg",
                COMPRESSED_IMAGE_DATA);
        when(imageCompressor.compressImage(multipart.getBytes())).thenReturn(COMPRESSED_IMAGE_DATA);
        when(beerRepository.findById(ID)).thenReturn(Optional.of(beer));

        imageService.addImageToBeer(ID, multipart);

        verify(imageCompressor, times(1)).compressImage(multipart.getBytes());
        verify(beerRepository, times(1)).findById(ID);

        ArgumentCaptor<Image> argumentCaptor = ArgumentCaptor.forClass(Image.class);
        verify(imageRepository, times(1)).save(argumentCaptor.capture());

        Image savedImage = argumentCaptor.getValue();
        assertEquals(IMAGE_NAME, savedImage.getName());
        assertEquals(beer, savedImage.getBeer());
        assertEquals(COMPRESSED_IMAGE_DATA, savedImage.getImage());
    }
    @Test
    void shouldThrowImageToCompressException() throws IOException {
        when(multipartFile.getBytes()).thenThrow(new IOException());

        ImageNotFoundException exception = assertThrows(ImageNotFoundException.class, () -> {
            imageService.addImageToBeer(ID, multipartFile);
        });
        String expectedMessage = String.format(IMAGE_COMPRESS_EXCEPTION, "");
        assertEquals(expectedMessage, exception.getMessage());
        verify(multipartFile).getBytes();
        verify(imageCompressor, never()).compressImage(any(byte[].class));
        verify(beerRepository, never()).findById(anyLong());
        verify(imageRepository, never()).save(any(Image.class));
    }
    @Test
    void shouldThrowBeerNotFoundException() throws IOException {
        byte[] fileBytes = new byte[0];
        when(multipartFile.getBytes()).thenReturn(fileBytes);
        when(imageCompressor.compressImage(fileBytes)).thenReturn(COMPRESSED_IMAGE_DATA);
        when(multipartFile.getOriginalFilename()).thenReturn(IMAGE_NAME);
        when(beerRepository.findById(BEER_ID)).thenReturn(Optional.empty());

        BeerNotFoundException exception = assertThrows(BeerNotFoundException.class, () -> {
            imageService.addImageToBeer(BEER_ID, multipartFile);
        });

        assertEquals(String.format(BEER_NOT_FOUND, BEER_ID), exception.getMessage());
        verify(beerRepository).findById(BEER_ID);
        verify(imageRepository, never()).save(any(Image.class));
    }
    @Test
    void shouldDeleteImage() {
        image.setName(IMAGE_NAME);

        when(imageRepository.findByName(IMAGE_NAME)).thenReturn(Optional.of(image));

        imageService.deleteImage(IMAGE_NAME);

        verify(imageRepository, times(1)).delete(image);
    }
}