package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ProductBundleImageMapper;
import com.example.hop_oasis.convertor.ProductBundleInfoMapper;
import com.example.hop_oasis.convertor.ProductBundleMapper;
import com.example.hop_oasis.decoder.ImageCompressor;
import com.example.hop_oasis.dto.ItemRatingDto;
import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.ProductBundleImage;
import com.example.hop_oasis.repository.ProductBundleImageRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.service.ProductBundleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class ProductBundleServiceImpl implements ProductBundleService {
    private final ProductBundleRepository productBundleRepository;
    private final ProductBundleImageRepository productImageRepository;
    private final ProductBundleMapper productBundleMapper;
    private final ProductBundleInfoMapper productBundleInfoMapper;
    private final ProductBundleImageMapper imageMapper;
    private final ImageCompressor imageCompressor;
    private final ProductBundleRatingServiceImpl productBundleRatingService;
    @Override
    public ProductBundle saveProductBundle(MultipartFile file, ProductBundleDto productBundleDto) {
        byte[] bytesIm;
        try {
            bytesIm = imageCompressor.compressImage(file.getBytes());
        } catch (IOException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        ProductBundleImage image = ProductBundleImage.builder()
                .image(bytesIm)
                .name(file.getOriginalFilename())
                .build();
        productBundleDto.getImageDto().add(imageMapper.toDto(image));

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
        return convertToDtoWithRating(productBundle);
    }
    @Override
    public ProductBundleInfoDto addRatingAndReturnUpdatedProductBundleInfo(Long id, double ratingValue) {
        if (ratingValue < 1.0 || ratingValue > 5.0) {
            throw new IllegalArgumentException("Rating value must be between 1 and 5");
        }
        productBundleRatingService.addRating(id, ratingValue);
        ProductBundle productBundle = productBundleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bundle not found with id " + id));
        return convertToDtoWithRating(productBundle);
    }
    private ProductBundleInfoDto convertToDtoWithRating(ProductBundle productBundle ) {
        ProductBundleInfoDto bundleInfoDto = productBundleInfoMapper.toDto(productBundle);
        ItemRatingDto rating = productBundleRatingService.getItemRating(productBundle.getId());
        BigDecimal roundedAverageRating = BigDecimal.valueOf(rating.getAverageRating())
                .setScale(1, RoundingMode.HALF_UP);
        bundleInfoDto.setAverageRating(roundedAverageRating.doubleValue());
        bundleInfoDto.setRatingCount(rating.getRatingCount());
        return bundleInfoDto;


    }
    @Override
    public Page<ProductBundleInfoDto> getAllProductBundle(Pageable pageable) {
        Page<ProductBundle> productBundles = productBundleRepository.findAll(pageable);
        if (productBundles.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        return productBundles.map(this::convertToDtoWithRating);
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
