package com.example.hop_oasis.service.data;
import com.example.hop_oasis.convertor.SnackInfoMapper;

import com.example.hop_oasis.dto.SnackInfoDto;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
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

import static com.example.hop_oasis.extractor.ImageNameExtractor.extractName;
import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class SnackImageServiceImpl implements SnackImageService {
    private final SnackRepository snackRepository;
    private final SnackImageRepository snackImageRepository;
    private final S3Service s3Service;
    private final SnackInfoMapper snackInfoMapper;


    @Override
    public SnackInfoDto addSnackImageToSnack(Long snackId, MultipartFile file) {
        Snack snack = snackRepository.findById(snackId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, snackId));
        String fileName;
        try {
            fileName = "snacks/" + file.getOriginalFilename();
            s3Service.uploadFile(fileName, file);

        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        SnackImage image1 = SnackImage.builder()
                .name(s3Service.getFileUrl(fileName).toString())
                .build();

        image1.setSnack(snack);
        snackImageRepository.save(image1);
        return snackInfoMapper.toDto(snackRepository.findById(snackId).get());
    }

    @Override
    public void deleteSnackImage(String name) {
        Optional<SnackImage> imageOp = snackImageRepository.findFirstByName(name);
        if (imageOp.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_DELETED, name);
        }
        s3Service.deleteFile(extractName(name));
        snackImageRepository.delete(imageOp.get());
    }
}
