package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ProductBundleInfoMapper;
import com.example.hop_oasis.convertor.ProductBundleMapper;
import com.example.hop_oasis.convertor.ProductBundleOptionsMapper;
import com.example.hop_oasis.dto.ItemRatingDto;
import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.ProductBundleOptions;
import com.example.hop_oasis.repository.ProductBundleOptionsRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.service.ProductBundleService;
import com.example.hop_oasis.utils.ProductBundleSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
@Log4j2
public class ProductBundleServiceImpl extends GeneralFilterService<ProductBundle, ProductBundleInfoDto> implements ProductBundleService {
    private final ProductBundleRepository productBundleRepository;
    private final ProductBundleMapper productBundleMapper;
    private final ProductBundleInfoMapper productBundleInfoMapper;
    private final ProductBundleRatingServiceImpl productBundleRatingService;
    private final ProductBundleOptionsRepository productBundleOptionsRepository;
    private final ProductBundleOptionsMapper productBundleOptionsMapper;

    @Override
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

    @Override
    public ProductBundleInfoDto getProductBundleById(Long id) {
        ProductBundle productBundle = productBundleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return convertToDtoWithRating(productBundle);
    }

    @Override
    public Page<ProductBundleInfoDto> getAllProductBundleWithFilter(String bundleName, Pageable pageable, String sortDirection) {
        Specification<ProductBundle> specification = Specification
                .where(ProductBundleSpecification.findByName(bundleName))
                .and(ProductBundleSpecification.sortByPrice(sortDirection));
      return getAllWithFilter(specification, productBundleRepository, pageable, this::convertToDtoWithRating);
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
    @Transactional
    public ProductBundleInfoDto update(ProductBundleDto productDto, Long id) {
        ProductBundle productBundle = productBundleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_DELETED, id));
        if (!productDto.getName().isEmpty()) {
            productBundle.setName(productDto.getName());
        }
        if (Objects.nonNull(productDto.getDescription())) {
            productBundle.setDescription(productDto.getDescription());
        }

        List<ProductBundleOptions> currentOptions = productBundle.getProductBundleOptions();
        List<ProductBundleOptions> newOptions = productBundleOptionsMapper.toEntity(productDto.getOptions());

        Map<Long, ProductBundleOptions> currentOptionsMap = currentOptions.stream()
                .collect(Collectors.toMap(ProductBundleOptions::getId, Function.identity()));

        for (ProductBundleOptions newOption : newOptions) {
            if (newOption.getId() != null) {
                ProductBundleOptions existingOption = currentOptionsMap.get(newOption.getId());
                if (existingOption != null) {
                    existingOption.setQuantity(newOption.getQuantity());
                    existingOption.setPrice(newOption.getPrice());
                } else {
                    newOption.setProductBundle(productBundle);
                    currentOptions.add(newOption);
                }
            } else {
                newOption.setProductBundle(productBundle);
                currentOptions.add(newOption);
            }
        }

        currentOptions.removeIf(option ->
                newOptions.stream().noneMatch(newOpt ->
                        Objects.equals(newOpt.getId(), option.getId()))
        );

        productBundle.setProductBundleOptions(currentOptions);
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
