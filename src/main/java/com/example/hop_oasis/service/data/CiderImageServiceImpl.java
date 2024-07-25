package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.CiderImageUrlDto;
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
    private final CiderRepository ciderRepository;
    private final S3Service s3Service;
    @Override
    public CiderImageUrlDto getCiderImageByName(String name) {
     return new CiderImageUrlDto(s3Service.getFileUrl(name).toString());
    }
    @Override
    public CiderImageUrlDto addCiderImageToCider(Long ciderId, MultipartFile file) {
        try {
            String fileName ="ciders/" + file.getOriginalFilename();
            s3Service.uploadFile(fileName, file);
        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        CiderImage image1 = CiderImage.builder()
                .name(file.getOriginalFilename())
                .build();
        Cider cider = ciderRepository.findById(ciderId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, ciderId));

        image1.setCider(cider);
        ciderImageRepository.save(image1);
       return new CiderImageUrlDto(s3Service.getFileUrl(image1.getName()).toString());
    }
    @Override
    public void deleteCiderImage(String name) {
        Optional<CiderImage> imageOp = ciderImageRepository.findFirstByName(name);
        if (imageOp.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, name);
        }
        s3Service.deleteFile("ciders/" +name);
        ciderImageRepository.delete(imageOp.get());
    }
}
