package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
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

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Optional;

import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {
    private static final String IMAGE_NAME = "image.jpg";
    private static final Long ID = 1L;
    private static final Long BEER_ID = 1L;
    private static final byte[] COMPRESSED_IMAGE_DATA = new byte[]{/* Compressed data*/};

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private BeerRepository beerRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private BeerInfoMapper beerInfoMapper;

    @Mock
    private MockMultipartFile multipartFile;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Mock
    private Beer beer;

    @Mock
    private Image image;

    @BeforeEach
    void setUp() {
        image = new Image();
        image.setName(IMAGE_NAME);
    }

    @Test
    void shouldAddImageToBeer() throws MalformedURLException {
        MultipartFile multipart = new MockMultipartFile(
                "file",
                IMAGE_NAME,
                "image/jpeg",
                COMPRESSED_IMAGE_DATA);
        final var imageUrl = "http://server/beers/" + IMAGE_NAME;
        when(beerRepository.findById(ID)).thenReturn(Optional.of(beer));
        when(s3Service.getFileUrl(anyString())).thenReturn(URI.create(imageUrl).toURL());
        when(beerInfoMapper.toDto(any(Beer.class))).thenReturn(any(BeerInfoDto.class));

        imageService.addImageToBeer(ID, multipart);

        verify(beerRepository, times(2)).findById(ID);

        ArgumentCaptor<Image> argumentCaptor = ArgumentCaptor.forClass(Image.class);
        verify(imageRepository, times(1)).save(argumentCaptor.capture());

        Image savedImage = argumentCaptor.getValue();
        assertEquals(imageUrl, savedImage.getName());
        assertEquals(beer, savedImage.getBeer());
    }


    @Test
    void shouldThrowBeerNotFoundException() {
        when(beerRepository.findById(BEER_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> imageService.addImageToBeer(BEER_ID, multipartFile));

        assertEquals(String.format(RESOURCE_NOT_FOUND, BEER_ID), exception.getMessage());
        verify(beerRepository).findById(BEER_ID);
        verify(imageRepository, never()).save(any(Image.class));
    }

    @Test
    void shouldDeleteImage() {
        image.setName(IMAGE_NAME);

        when(imageRepository.findFirstByName(IMAGE_NAME)).thenReturn(Optional.of(image));

        imageService.deleteImage(IMAGE_NAME);

        verify(imageRepository, times(1)).delete(image);
    }
}