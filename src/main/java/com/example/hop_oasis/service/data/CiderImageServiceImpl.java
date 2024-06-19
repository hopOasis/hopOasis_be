package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.CiderImageMapper;
import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.CiderImageDto;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.CiderImage;
import com.example.hop_oasis.repository.CiderImageRepository;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.service.CiderImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CiderImageServiceImpl implements CiderImageService {
    private final CiderImageRepository ciderImageRepository;
    private final CiderImageMapper ciderImageMapper;
    private final ImageCompressor imageCompressor;
    private final CiderRepository ciderRepository;

    @Override
    public CiderImageDto getCiderImageByName(String name) {
        Optional<CiderImage> imageOp = ciderImageRepository.findByName(name);
        if (imageOp.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, name);
        }
        CiderImage image = imageOp.get();
        image.setImage(imageCompressor.decompressImage(image.getImage(), name));
        return ciderImageMapper.toDto(image);
    }
    @Override
    public void addCiderImageToCider(Long ciderId, MultipartFile file) {
        byte[] image;
        try {
            image = imageCompressor.compressImage(file.getBytes());
        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        CiderImage image1 = CiderImage.builder()
                .image(image)
                .name(file.getOriginalFilename())
                .build();
        Cider cider = ciderRepository.findById(ciderId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, ciderId));

        image1.setCider(cider);
        ciderImageRepository.save(image1);
    }
    @Override
    public void deleteCiderImage(String name) {
        Optional<CiderImage> imageOp = ciderImageRepository.findByName(name);
        if (imageOp.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, name);
        }
        ciderImageRepository.delete(imageOp.get());
    }
}
