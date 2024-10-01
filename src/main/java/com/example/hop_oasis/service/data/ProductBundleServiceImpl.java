package com.example.hop_oasis.service.data;
import com.example.hop_oasis.convertor.ProductBundleInfoMapper;
import com.example.hop_oasis.convertor.ProductBundleMapper;
import com.example.hop_oasis.dto.ItemRatingDto;
import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.dto.ProductBundleOptionsDto;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.ProductBundleOptions;
import com.example.hop_oasis.repository.ProductBundleOptionsRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.service.ProductBundleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;


import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductBundleServiceImpl implements ProductBundleService {
    private final ProductBundleRepository productBundleRepository;
    private final ProductBundleMapper productBundleMapper;
    private final ProductBundleInfoMapper productBundleInfoMapper;
    private final ProductBundleRatingServiceImpl productBundleRatingService;
    private final ProductBundleOptionsRepository productBundleOptionsRepository;
    @Override
    public ProductBundle saveProductBundle(ProductBundleDto productBundleDto) {
        ProductBundle productBundle = productBundleMapper.toEntity(productBundleDto);
        productBundleRepository.save(productBundle);
        for (ProductBundleOptionsDto optionsDto : productBundleDto.getOptions()) {
            ProductBundleOptions options = new ProductBundleOptions();
            options.setProductBundle(productBundle);
            options.setQuantity(optionsDto.getQuantity());
            options.setPrice(optionsDto.getPrice());
            productBundleOptionsRepository.save(options);
        }
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

        productBundleRatingService.addRating(id, ratingValue);
        ProductBundle productBundle = productBundleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bundle not found with id " + id));
        return convertToDtoWithRating(productBundle);
    }
    private ProductBundleInfoDto convertToDtoWithRating(ProductBundle productBundle) {
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
            return Page.empty(pageable);
        }
        return productBundles.map(this::convertToDtoWithRating);
    }

    @Override
    @Transactional
    public ProductBundleInfoDto update(ProductBundleDto productDto, Long id) {
        ProductBundle productBundle = productBundleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        if (!productDto.getName().isEmpty()) {
            productBundle.setName(productDto.getName());
        }

        if (Objects.nonNull(productDto.getDescription())) {
            productBundle.setDescription(productDto.getDescription());
        }
        for (ProductBundleOptionsDto optionsDto : productDto.getOptions()) {
            ProductBundleOptions options = new ProductBundleOptions();
            options.setQuantity(optionsDto.getQuantity());
            options.setPrice(optionsDto.getPrice());
            productBundleOptionsRepository.save(options);
        }
        return productBundleInfoMapper.toDto(productBundleRepository.save(productBundle));
    }
    @Override
    @Transactional
    public ProductBundleInfoDto deleteProductBundle(Long id) {
        ProductBundle productBundle = productBundleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        productBundleRepository.deleteById(id);
        return productBundleInfoMapper.toDto(productBundle);
    }
}
