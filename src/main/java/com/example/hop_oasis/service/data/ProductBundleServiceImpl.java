package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ProductBundleImageMapper;
import com.example.hop_oasis.convertor.ProductBundleInfoMapper;
import com.example.hop_oasis.convertor.ProductBundleMapper;
import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleImageDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.ProductBundleImage;
import com.example.hop_oasis.repository.ProductBundleImageRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.service.ProductBundleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class ProductBundleServiceImpl implements ProductBundleService {
    private final ProductBundleRepository productBundleRepository;
    private final ProductBundleImageRepository productImageRepository;
    private final ProductBundleMapper productBundleMapper;
    private final ProductBundleInfoMapper productBundleInfoMapper;
    private final ProductBundleImageMapper imageMapper;
    private final ImageCompressor imageCompressor;
    @Override
    public ProductBundle saveProductBundle(MultipartFile file, ProductBundleDto productBundleDto) {
        byte[] bytesIm;
        try {
            bytesIm = imageCompressor.compressImage(file.getBytes());
        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND,"");
        }
        ProductBundleImage image = ProductBundleImage.builder()
                .image(bytesIm)
                .name(file.getOriginalFilename())
                .build();
        List<ProductBundleImageDto> images = new ArrayList<>();
        images.add(imageMapper.toDto(image));

        productBundleDto.setImageDto(images);
        ProductBundle productBundle = productBundleMapper.toEntity(productBundleDto);
        productBundleRepository.save(productBundle);
        image.setProductBundle(productBundle);
        productImageRepository.save(image);
        return productBundle;
    }
    @Override
    public ProductBundleInfoDto getProductBundleById(Long id) {
        ProductBundle productBundle = productBundleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return productBundleInfoMapper.toDto(productBundle);
    }
    @Override
    public Page<ProductBundleInfoDto> getAllProductBundle(Pageable pageable) {
        Page<ProductBundle> productBundles = productBundleRepository.findAll(pageable);
        if (productBundles.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        return productBundles.map(productBundleInfoMapper::toDto);
    }
    @Override
    public ProductBundleInfoDto update(ProductBundleInfoDto productDto, Long id) {
        ProductBundle productBundle = productBundleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        if (!productDto.getName().isEmpty()) {
            productBundle.setName(productDto.getName());
        }
        if (productDto.getPrice() != 0.0) {
            productBundle.setPrice(productDto.getPrice());
        }
        if (!productDto.getDescription().isEmpty()) {
            productBundle.setDescription(productDto.getDescription());
        }
        return productBundleInfoMapper.toDto(productBundleRepository.save(productBundle));
    }
    @Override
    public ProductBundleInfoDto deleteProductBundle(Long id) {
        ProductBundle productBundle = productBundleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        productBundleRepository.deleteById(id);
        return productBundleInfoMapper.toDto(productBundle);
    }
}
