package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.SnackImageMapper;
import com.example.hop_oasis.convertor.SnackInfoMapper;
import com.example.hop_oasis.convertor.SnackMapper;
import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.SnackDto;
import com.example.hop_oasis.dto.SnackImageDto;
import com.example.hop_oasis.dto.SnackInfoDto;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Snack;
import com.example.hop_oasis.model.SnackImage;
import com.example.hop_oasis.repository.SnackImageRepository;
import com.example.hop_oasis.repository.SnackRepository;
import com.example.hop_oasis.service.SnackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class SnackServiceImpl implements SnackService {
    private final SnackRepository snackRepository;
    private final SnackImageRepository snackImageRepository;
    private final SnackMapper snackMapper;
    private final SnackInfoMapper snackInfoMapper;
    private final SnackImageMapper snackImageMapper;
    private final ImageCompressor imageCompressor;
    @Override
    public Snack saveSnack(MultipartFile file, SnackDto snackDto) {
        byte[] bytesIm;
        try {
            bytesIm = imageCompressor.compressImage(file.getBytes());
        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        SnackImage image = SnackImage.builder()
                .image(bytesIm)
                .name(file.getOriginalFilename())
                .build();
        List<SnackImageDto> images = new ArrayList<>();
        images.add(snackImageMapper.toDto(image));
        snackDto.setSnackImageDto(images);
        Snack snack = snackMapper.toEntity(snackDto);
        snackRepository.save(snack);
        image.setSnack(snack);
        snackImageRepository.save(image);
        return snack;
    }
    @Override
    public SnackInfoDto getSnackById(Long id) {
        Snack snack = snackRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return snackInfoMapper.toDto(snack);
    }
    @Override
    public Page<SnackInfoDto> getAllSnacks(Pageable pageable) {
        Page<Snack> snacks = snackRepository.findAll(pageable);
        if (snacks.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        return snacks.map(snackInfoMapper::toDto);
    }
    @Override
    public SnackInfoDto updateSnack(SnackInfoDto snackInfo, Long id) {
        Snack snack = snackRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));

        if (!snackInfo.getSnackName().isEmpty()) {
            snack.setSnackName(snackInfo.getSnackName());
        }
        if (snackInfo.getWeightLarge() != 0.0) {
            snack.setWeightLarge(snackInfo.getWeightLarge());
        }
        if (snackInfo.getWeightSmall() != 0.0) {
            snack.setWeightSmall(snackInfo.getWeightSmall());
        }
        if (snackInfo.getPriceLarge() != 0.0) {
            snack.setPriceLarge(snackInfo.getPriceLarge());
        }
        if (snackInfo.getPriceSmall() != 0.0) {
            snack.setPriceSmall(snackInfo.getPriceSmall());
        }
        if (!snackInfo.getDescription().isEmpty()) {
            snack.setDescription(snackInfo.getDescription());
        }
       return snackInfoMapper.toDto(snackRepository.save(snack));
    }
    @Override
    public SnackInfoDto deleteSnack(Long id) {
        Snack snack = snackRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        snackRepository.deleteById(id);
        return snackInfoMapper.toDto(snack);
    }
}
