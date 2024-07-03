package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.CiderImageMapper;
import com.example.hop_oasis.convertor.CiderInfoMapper;
import com.example.hop_oasis.convertor.CiderMapper;
import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.CiderDto;
import com.example.hop_oasis.dto.CiderImageDto;
import com.example.hop_oasis.dto.CiderInfoDto;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.CiderImage;
import com.example.hop_oasis.repository.CiderImageRepository;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.service.CiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.RESOURCE_DELETED;
import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CiderServiceImpl implements CiderService {
    private final CiderRepository ciderRepository;
    private final CiderImageRepository ciderImageRepository;
    private final CiderMapper ciderMapper;
    private final CiderInfoMapper ciderInfoMapper;
    private final CiderImageMapper ciderImageMapper;
    private final ImageCompressor imageCompressor;

    @Override
    public Cider saveCider(MultipartFile file, CiderDto ciderDto) {
        byte[] bytesIm;
        try {
            bytesIm = imageCompressor.compressImage(file.getBytes());
        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        CiderImage image = CiderImage.builder()
                .image(bytesIm)
                .name(file.getOriginalFilename())
                .build();
        List<CiderImageDto> images = new ArrayList<>();
        images.add(ciderImageMapper.toDto(image));
        ciderDto.setImage(images);
        Cider cider = ciderMapper.toEntity(ciderDto);
        ciderRepository.save(cider);
        image.setCider(cider);
        ciderImageRepository.save(image);

        return cider;
    }

    @Override
    public CiderInfoDto getCiderById(Long id) {
        Cider cider = ciderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return ciderInfoMapper.toDto(cider);
    }

    @Override
    public Page<CiderInfoDto> getAllCiders(Pageable pageable) {
        Page<Cider> cider = ciderRepository.findAll(pageable);
        if (cider.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        return cider.map(ciderInfoMapper::toDto);
    }

    @Override
    public CiderInfoDto update(CiderInfoDto ciderInfo, Long id) {
        Cider cider = ciderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        if (!ciderInfo.getCiderName().isEmpty()) {
            cider.setCiderName(ciderInfo.getCiderName());
        }
        if (ciderInfo.getVolumeLarge() != 0.0) {
            cider.setVolumeLarge(ciderInfo.getVolumeLarge());
        }
        if (ciderInfo.getVolumeSmall() != 0.0) {
            cider.setVolumeSmall(ciderInfo.getVolumeSmall());
        }
        if (ciderInfo.getPriceLarge() != 0.0) {
            cider.setPriceLarge(ciderInfo.getPriceLarge());
        }
        if (ciderInfo.getPriceSmall() != 0.0) {
            cider.setPriceSmall(ciderInfo.getPriceSmall());
        }
        if (!ciderInfo.getDescription().isEmpty()) {
            cider.setDescription(ciderInfo.getDescription());
        }
        return ciderInfoMapper.toDto(ciderRepository.save(cider));
    }


    @Override
    public CiderInfoDto deleteCider(Long id) {
        Cider cider = ciderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        ciderRepository.deleteById(id);
        return ciderInfoMapper.toDto(cider);

    }
}
