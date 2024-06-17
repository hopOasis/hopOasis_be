package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ImageMapper;
import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.ImageDto;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.Image;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.ImageRepository;
import com.example.hop_oasis.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final ImageCompressor imageCompressor;
    private final BeerRepository beerRepository;
    @Override
    public ImageDto getImageByName(String name) {
        Optional<Image> imageOp = imageRepository.findByName(name);
        if (imageOp.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, name);
        }
        Image image = imageOp.get();
        image.setImage(imageCompressor.decompressImage(image.getImage(), name));
        return imageMapper.toDto(image);
    }
    @Override
    public void addImageToBeer(Long beerId, MultipartFile file) {
        byte[] image = new byte[0];
        try {
            image = imageCompressor.compressImage(file.getBytes());
        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND,"");
        }
        Image image1 = Image.builder()
                .image(image)
                .name(file.getOriginalFilename())
                .build();
        Beer beer = beerRepository.findById(beerId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, beerId));

        image1.setBeer(beer);
        imageRepository.save(image1);

    }
    @Override
    public void deleteImage(String name) {
        Optional<Image> imageOp = imageRepository.findByName(name);
        if (imageOp.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, name);
        }
        imageRepository.delete(imageOp.get());
    }
}
