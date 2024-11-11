package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.BeerInfoMapper;
import com.example.hop_oasis.convertor.CiderInfoMapper;
import com.example.hop_oasis.convertor.ProductBundleInfoMapper;
import com.example.hop_oasis.convertor.SnackInfoMapper;
import com.example.hop_oasis.dto.ItemInfoDto;
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
    private static double averageRating;
    private static int ratingCount;

    @Override
    public Page<ItemInfoDto> getAllItems(Pageable pageable) {
        int loadSize = pageable.getPageSize() + 5;
        List<ItemInfoDto> allItems = new ArrayList<>();

        allItems.addAll(beerRepository.findAll(PageRequest.of(0, loadSize)).stream()
                .map(beer -> {
                    averageRating = beerRatingService.getAverageRating(beer.getId());
                    ratingCount = beerRatingService.getRatingCount(beer.getId());
                    return beerInfoMapper.mapToItemInfoDto(beer, averageRating, ratingCount);
                })
                .toList()
        );

        allItems.addAll(ciderRepository.findAll(PageRequest.of(0, loadSize)).stream()
                .map(cider -> {
                    averageRating = ciderRatingService.getAverageRating(cider.getId());
                    ratingCount = ciderRatingService.getRatingCount(cider.getId());
                    return ciderInfoMapper.mapToItemInfoDto(cider, averageRating, ratingCount);
                })
                .toList()
        );

        allItems.addAll(snackRepository.findAll(PageRequest.of(0, loadSize)).stream()
                .map(snack -> {
                    averageRating = snackRatingService.getAverageRating(snack.getId());
                    ratingCount = snackRatingService.getRatingCount(snack.getId());
                    return snackInfoMapper.mapToItemInfoDto(snack, averageRating, ratingCount);
                })
                .toList()
        );

        allItems.addAll(bundleRepository.findAll(PageRequest.of(0, loadSize)).stream()
                .map(bundle -> {
                    averageRating = productBundleRatingService.getAverageRating(bundle.getId());
                    ratingCount = productBundleRatingService.getRatingCount(bundle.getId());
                    return bundleInfoMapper.mapToItemInfoDto(bundle, averageRating, ratingCount);
                })
                .toList()
        );
        Collections.shuffle(allItems);
        int start = Math.min((int) pageable.getOffset(), allItems.size());
        int end = Math.min(start + pageable.getPageSize(), allItems.size());
        return new PageImpl<>(allItems.subList(start, end), pageable, allItems.size());
    }

}
