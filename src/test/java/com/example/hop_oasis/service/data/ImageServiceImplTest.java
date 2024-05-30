package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ImageMapper;
import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.ImageDto;
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

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {
    private static final String imageName = "image.jpg";
    private static final Long beerId = 1L;
    private static final byte[] compressedImageData = new byte[]{/* Compressed data*/};

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

    @BeforeEach
    void setUp() {
        multipartFile = new MockMultipartFile("file", imageName, "image/jpeg", compressedImageData);
    }

    @Test
    void shouldReturnImageByName() {
        byte[] decompressedImageData = new byte[]{/* Decompressed data*/};
        Image image = new Image();
        image.setImage(compressedImageData);
        image.setName(imageName);
        ImageDto imageDto = new ImageDto();

        when(imageCompressor.decompressImage(eq(compressedImageData), eq(imageName))).thenReturn(decompressedImageData);
        when(imageRepository.findByName(eq(imageName))).thenReturn(Optional.of(image));
        when(imageMapper.toDto(eq(image))).thenReturn(imageDto);

        ImageDto returnedImageDto = imageService.getImageByName(imageName);

        assertNotNull(returnedImageDto);
        assertEquals(imageDto, returnedImageDto);
        verify(imageRepository, times(1)).findByName(eq(imageName));
        verify(imageCompressor, times(1)).decompressImage(eq(compressedImageData), eq(imageName));
        verify(imageMapper, times(1)).toDto(eq(image));
    }

    @Test
    void shouldThrowImageFoundException() {
        when(imageRepository.findByName(imageName)).thenReturn(Optional.empty());
        assertThrows(ImageNotFoundException.class, () -> {
            imageService.getImageByName(imageName);
        });
        try {
            imageService.getImageByName(imageName);
            fail("Expected ImageNotFoundException");
        } catch (ImageNotFoundException e) {
            assertEquals("Image [" + imageName + "] not found", e.getMessage());
        }
        verify(imageRepository, times(2)).findByName(imageName);

    }

    @Test
    void shouldAddImageToBeer() throws IOException {
        Image image = Image.builder()
                .image(compressedImageData)
                .name(imageName)
                .build();

        when(imageCompressor.compressImage(multipartFile.getBytes())).thenReturn(compressedImageData);
        when(beerRepository.findById(beerId)).thenReturn(Optional.of(beer));

        imageService.addImageToBeer(beerId, multipartFile);

        verify(imageCompressor, times(1)).compressImage(multipartFile.getBytes());
        verify(beerRepository, times(1)).findById(beerId);

        ArgumentCaptor<Image> argumentCaptor = ArgumentCaptor.forClass(Image.class);
        verify(imageRepository, times(1)).save(argumentCaptor.capture());

        Image savedImage = argumentCaptor.getValue();
        assertEquals(imageName, savedImage.getName());
        assertEquals(beer, savedImage.getBeer());
        assertEquals(compressedImageData, savedImage.getImage());

    }

    @Test
    void shouldThrowImageToBeerException() {
        when(imageCompressor.compressImage(any(byte[].class))).thenReturn(compressedImageData);
        when(beerRepository.findById(beerId)).thenReturn(Optional.of(beer));

        doThrow(new ImageNotFoundException("Failed to save image to the database", compressedImageData)).when(imageRepository).save(any(Image.class));
        assertThrows(ImageNotFoundException.class, () -> imageService.addImageToBeer(beerId, multipartFile));

        verify(imageCompressor, times(1)).compressImage(any(byte[].class));
        verify(beerRepository, times(1)).findById(beerId);
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void shouldDeleteImage() {
        image.setName(imageName);

        when(imageRepository.findByName(imageName)).thenReturn(Optional.of(image));

        imageService.deleteImage(imageName);

        verify(imageRepository, times(1)).delete(image);
    }
}