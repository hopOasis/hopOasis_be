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
import java.util.Arrays;
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
        long beerCount = beerRepository.count();
        long ciderCount = ciderRepository.count();
        long snackCount = snackRepository.count();
        long bundleCount = bundleRepository.count();
        long totalElements = beerCount + ciderCount + snackCount + bundleCount;

        if (pageable.getOffset() >= totalElements) {
            return new PageImpl<>(Collections.emptyList(), pageable, totalElements);
        }
        List<String> repositoriesOrder = Arrays.asList("beer", "cider", "snack", "bundle");

        Collections.shuffle(repositoriesOrder);

        int remaining = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        List<ItemInfoDto> currentPageItems = new ArrayList<>();

        for (String repo : repositoriesOrder) {
            if (remaining <= 0)
                break;
            if ("beer".equals(repo)) {
                if (offset < beerCount) {
                    Page<Beer> beerPage = beerRepository.findAll(PageRequest.of(offset / pageable.getPageSize(),
                            Math.min(remaining, (int) beerCount - offset)));
                    List<ItemInfoDto> beerItems = mapItemsWithRating(beerPage.getContent(), beerInfoMapper);
                    currentPageItems.addAll(beerItems);
                    remaining -= beerItems.size();
                }
                offset = Math.max(0, offset - (int) beerCount);
            } else if ("cider".equals(repo)) {
                if (offset < ciderCount) {
                    Page<Cider> ciderPage = ciderRepository.findAll(PageRequest.of(offset / pageable.getPageSize(),
                            Math.min(remaining, (int) ciderCount - offset)));
                    List<ItemInfoDto> ciderItems = mapItemsWithRating(ciderPage.getContent(), ciderInfoMapper);
                    currentPageItems.addAll(ciderItems);
                    remaining -= ciderItems.size();
                }
                offset = Math.max(0, offset - (int) ciderCount);
            } else if ("snack".equals(repo)) {
                if (offset < snackCount) {
                    Page<Snack> snackPage = snackRepository.findAll(PageRequest.of(offset / pageable.getPageSize(),
                            Math.min(remaining, (int) snackCount - offset)));
                    List<ItemInfoDto> snackItems = mapItemsWithRating(snackPage.getContent(), snackInfoMapper);
                    currentPageItems.addAll((snackItems));
                    remaining -= snackItems.size();
                }
                offset = Math.max(0, offset - (int) snackCount);
            } else if ("bundle".equals(repo)) {
                if (offset < bundleCount) {
                    Page<ProductBundle> bundlePage = bundleRepository.findAll(PageRequest.of(offset / pageable.getPageSize(),
                            Math.min(remaining, (int) bundleCount - offset)));
                    List<ItemInfoDto> bundleItems = mapItemsWithRating(bundlePage.getContent(), bundleInfoMapper);
                    currentPageItems.addAll(bundleItems);
                    remaining -= bundleItems.size();
                }
                offset = Math.max(0, offset - (int) bundleCount);
            }
            Collections.shuffle(currentPageItems);
        }
        return new PageImpl<>(currentPageItems, pageable, totalElements);
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

