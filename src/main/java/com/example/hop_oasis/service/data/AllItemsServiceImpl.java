package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.*;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.Snack;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.repository.SnackRepository;
import com.example.hop_oasis.utils.BeerSpecification;
import com.example.hop_oasis.utils.CiderSpecification;
import com.example.hop_oasis.utils.ProductBundleSpecification;
import com.example.hop_oasis.utils.SnackSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AllItemsServiceImpl {
    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;
    private final ProductBundleRepository bundleRepository;
    private final BeerInfoMapper beerInfoMapper;
    private final CiderInfoMapper ciderInfoMapper;
    private final SnackInfoMapper snackInfoMapper;
    private final ProductBundleInfoMapper bundleInfoMapper;
    private final BeerRatingServiceImpl beerRatingService;
    private final CiderRatingServiceImpl ciderRatingService;
    private final SnackRatingServiceImpl snackRatingService;
    private final ProductBundleRatingServiceImpl productBundleRatingService;

    private final ConcurrentHashMap<String, List<ItemInfoDto>> cache = new ConcurrentHashMap<>();

    public Page<ItemInfoDto> getAllItems(Pageable pageable, String name) {

        String cacheKey = "shuffled_items_" + name;

        List<ItemInfoDto> allItems = cache.computeIfAbsent(cacheKey, key -> {
            List<ItemInfoDto> items = new ArrayList<>();
            items.addAll(mapItemsWithRating(beerRepository.findAll(BeerSpecification.findByName(name)), beerInfoMapper));
            items.addAll(mapItemsWithRating(ciderRepository.findAll(CiderSpecification.findByName(name)), ciderInfoMapper));
            items.addAll(mapItemsWithRating(snackRepository.findAll(SnackSpecification.findByName(name)), snackInfoMapper));
            items.addAll(mapItemsWithRating(bundleRepository.findAll(ProductBundleSpecification.findByName(name)), bundleInfoMapper));

            Collections.shuffle(items);
            return items;
        });

        long totalElements = allItems.size();

        if (pageable.getOffset() >= totalElements) {
            return new PageImpl<>(Collections.emptyList(), pageable, totalElements);
        }

        List<ItemInfoDto> pageItems = allItems.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return new PageImpl<>(pageItems, pageable, totalElements);
    }


    private <T, M> ItemInfoDto mapToItemInfoDto(M mapper, T item) {
        try {
            Method method = mapper.getClass().getMethod("mapToItemInfoDto", item.getClass());
            return (ItemInfoDto) method.invoke(mapper, item);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking mapToItemInfoDto", e);
        }
    }

    private <T, M> List<ItemInfoDto> mapItemsWithRating(List<T> items, M mapper) {
        return items.stream()
                .map(item -> {
                    ItemInfoDto dto = mapToItemInfoDto(mapper, item);
                    dto.setAverageRating(getAverageRating(item));
                    dto.setRatingCount(getRatingCount(item));
                    return dto;
                })
                .toList();
    }

    private <T> double getAverageRating(T item) {
        if (item instanceof Beer) {
            return beerRatingService.getAverageRating(((Beer) item).getId());
        } else if (item instanceof Cider) {
            return ciderRatingService.getAverageRating(((Cider) item).getId());
        } else if (item instanceof Snack) {
            return snackRatingService.getAverageRating(((Snack) item).getId());
        } else if (item instanceof ProductBundle) {
            return productBundleRatingService.getAverageRating(((ProductBundle) item).getId());
        }
        return 0.0;
    }

    private <T> int getRatingCount(T item) {
        if (item instanceof Beer) {
            return beerRatingService.getRatingCount(((Beer) item).getId());
        } else if (item instanceof Cider) {
            return ciderRatingService.getRatingCount(((Cider) item).getId());
        } else if (item instanceof Snack) {
            return snackRatingService.getRatingCount(((Snack) item).getId());
        } else if (item instanceof ProductBundle) {
            return productBundleRatingService.getRatingCount(((ProductBundle) item).getId());
        }
        return 0;
    }
}

