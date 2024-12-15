package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.CiderInfoMapper;
import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.CiderImage;
import com.example.hop_oasis.repository.CiderImageRepository;
import com.example.hop_oasis.repository.CiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.example.hop_oasis.extractor.ImageNameExtractor.extractName;
import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CiderImageServiceImpl {
    private final CiderImageRepository ciderImageRepository;
    private final CiderRepository ciderRepository;
    private final S3Service s3Service;
    private final CiderInfoMapper ciderInfoMapper;


    public CiderInfoDto addCiderImageToCider(Long ciderId, MultipartFile file) {
        Cider cider = ciderRepository.findById(ciderId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, ciderId));
        String fileName;
        try {
            fileName = "ciders/" + file.getOriginalFilename();
            s3Service.uploadFile(fileName, file);
        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        CiderImage image1 = CiderImage.builder()
                .name(s3Service.getFileUrl(fileName).toString())
                .build();

        image1.setCider(cider);
        ciderImageRepository.save(image1);
       return ciderInfoMapper.toDto(ciderRepository.findById(ciderId).get());

    }
    public void deleteCiderImage(String name) {
        Optional<CiderImage> imageOp = ciderImageRepository.findFirstByName(name);
        if (imageOp.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, name);
        }
        s3Service.deleteFile(extractName(name));
        ciderImageRepository.delete(imageOp.get());
    }
}
