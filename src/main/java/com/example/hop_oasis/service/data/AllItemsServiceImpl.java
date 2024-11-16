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
import com.example.hop_oasis.service.AllItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AllItemsServiceImpl implements AllItemsService {
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

    @Override
    public Page<ItemInfoDto> getAllItems(Pageable pageable) {
        int loadSize = pageable.getPageSize() + 5;
        List<ItemInfoDto> allItems = new ArrayList<>();

        allItems.addAll(mapItemsWithRating(beerRepository.findAll(PageRequest.of(0, loadSize)).getContent(),
                beerInfoMapper));

        allItems.addAll(mapItemsWithRating(ciderRepository.findAll(PageRequest.of(0, loadSize)).getContent(),
                ciderInfoMapper));

        allItems.addAll(mapItemsWithRating(snackRepository.findAll(PageRequest.of(0, loadSize)).getContent(),
                snackInfoMapper));

        allItems.addAll(mapItemsWithRating(bundleRepository.findAll(PageRequest.of(0, loadSize)).getContent(),
                bundleInfoMapper));
        Collections.shuffle(allItems);
        int start = Math.min((int) pageable.getOffset(), allItems.size());
        int end = Math.min(start + pageable.getPageSize(), allItems.size());
        return new PageImpl<>(allItems.subList(start, end), pageable, allItems.size());
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

