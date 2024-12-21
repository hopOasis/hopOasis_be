package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ProductBundleInfoMapper;
import com.example.hop_oasis.convertor.ProductBundleMapper;
import com.example.hop_oasis.convertor.ProductBundleOptionsMapper;
import com.example.hop_oasis.dto.ItemRatingDto;
import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.BeerOptions;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.ProductBundleOptions;
import com.example.hop_oasis.repository.ProductBundleOptionsRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.utils.ProductBundleSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductBundleServiceImpl {
    private final ProductBundleRepository productBundleRepository;
    private final ProductBundleMapper productBundleMapper;
    private final ProductBundleInfoMapper productBundleInfoMapper;
    private final ProductBundleRatingServiceImpl productBundleRatingService;
    private final ProductBundleOptionsRepository productBundleOptionsRepository;
    private final ProductBundleOptionsMapper productBundleOptionsMapper;


    public ProductBundle saveProductBundle(ProductBundleDto productBundleDto) {
        ProductBundle productBundle = productBundleMapper.toEntity(productBundleDto);
        List<ProductBundleOptions> productBundleOptionsList = productBundleOptionsMapper
                .toEntity(productBundleDto.getOptions());
        for (ProductBundleOptions options : productBundleOptionsList) {
            options.setProductBundle(productBundle);
        }

        productBundle.setProductBundleOptions(productBundleOptionsList);
        productBundleRepository.save(productBundle);
        return productBundle;
    }

    public ProductBundleInfoDto getProductBundleById(Long id) {
        ProductBundle productBundle = productBundleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return convertToDtoWithRating(productBundle);
    }

    public Page<ProductBundleInfoDto> getAllProductBundleWithFilter(String bundleName, Pageable pageable, String sortDirection) {
       Page<ProductBundle> bundles = productBundleRepository
               .findAll(ProductBundleSpecification.filterAndSort(bundleName, sortDirection), pageable);
       return bundles.map(this::convertToDtoWithRating);
    }

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


    @Transactional
    public ProductBundleInfoDto update(ProductBundleDto productDto, Long id) {
        ProductBundle productBundle = productBundleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_DELETED, id));
        if (Objects.nonNull(productDto.getName())) {
            productBundle.setName(productDto.getName());
        }
        if (Objects.nonNull(productDto.getDescription())) {
            productBundle.setDescription(productDto.getDescription());
        }

        List<ProductBundleOptions> currentOptions = productBundle.getProductBundleOptions();

        if (Objects.nonNull(productDto.getOptions())) {
            List<ProductBundleOptions> newOptions = productBundleOptionsMapper.toEntity(productDto.getOptions());
            for (ProductBundleOptions curren : currentOptions) {
                for (ProductBundleOptions newOption : newOptions) {
                    if (curren.getId() == newOption.getId()) {
                        if (newOption.getQuantity() != 0) {
                            curren.setQuantity(newOption.getQuantity());
                        }
                        if (newOption.getPrice() != 0) {
                            curren.setPrice(newOption.getPrice());
                        }
                    }
                }

            }
            productBundle.setProductBundleOptions(currentOptions);
        }
        return productBundleInfoMapper.toDto(productBundleRepository.save(productBundle));
    }


    @Transactional
    public ProductBundleInfoDto deleteProductBundle(Long id) {
        ProductBundle productBundle = productBundleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        productBundleRepository.deleteById(id);
        return productBundleInfoMapper.toDto(productBundle);
    }
}
