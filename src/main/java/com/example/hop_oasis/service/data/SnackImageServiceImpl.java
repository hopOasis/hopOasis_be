package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.SnackImageMapper;
import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.SnackImageDto;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Snack;
import com.example.hop_oasis.model.SnackImage;
import com.example.hop_oasis.repository.SnackImageRepository;
import com.example.hop_oasis.repository.SnackRepository;
import com.example.hop_oasis.service.SnackImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class SnackImageServiceImpl implements SnackImageService {
    private final SnackRepository snackRepository;
    private final SnackImageMapper snackImageMapper;
    private final ImageCompressor imageCompressor;
    private final SnackImageRepository snackImageRepository;
    @Override
    public SnackImageDto getSnackImageByName(String name) {
        Optional<SnackImage> imageOp = snackImageRepository.findFirstByName(name);
        if (imageOp.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, name);
        }
        SnackImage image = imageOp.get();
        image.setImage(imageCompressor.decompressImage(image.getImage(), name));
        return snackImageMapper.toDto(image);
    }
    @Override
    public SnackImage addSnackImageToSnack(Long snackId, MultipartFile file) {
        byte[] image = new byte[0];
        try {
            image = imageCompressor.compressImage(file.getBytes());

        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        SnackImage image1 = SnackImage.builder()
                .image(image)
                .name(file.getOriginalFilename())
                .build();
        Snack snack = snackRepository.findById(snackId).orElseThrow(()->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, ""));
        image1.setSnack(snack);
        snackImageRepository.save(image1);
        return image1;
    }
    @Override
    public void deleteSnackImage(String name) {
        Optional<SnackImage> imageOp = snackImageRepository.findFirstByName(name);
        if (imageOp.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_DELETED, name);
        }
        snackImageRepository.delete(imageOp.get());
    }
}
