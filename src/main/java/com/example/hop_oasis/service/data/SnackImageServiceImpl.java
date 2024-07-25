package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.SnackImageUrlDto;
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
    private final SnackImageRepository snackImageRepository;
    private final S3Service s3Service;

    @Override
    public SnackImageUrlDto getSnackImageByName(String name) {
        return new SnackImageUrlDto(s3Service.getFileUrl(name).toString());
    }

    @Override
    public SnackImageUrlDto addSnackImageToSnack(Long snackId, MultipartFile file) {
        try {
            String fileName = "snacks/" + file.getOriginalFilename();
            s3Service.uploadFile(fileName, file);

        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        SnackImage image1 = SnackImage.builder()
                .name(file.getOriginalFilename())
                .build();
        Snack snack = snackRepository.findById(snackId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, ""));
        image1.setSnack(snack);
        snackImageRepository.save(image1);
        return new SnackImageUrlDto(s3Service.getFileUrl(image1.getName()).toString());
    }

    @Override
    public void deleteSnackImage(String name) {
        Optional<SnackImage> imageOp = snackImageRepository.findFirstByName(name);
        if (imageOp.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_DELETED, name);
        }
        s3Service.deleteFile("snacks/" + name);
        snackImageRepository.delete(imageOp.get());
    }
}
