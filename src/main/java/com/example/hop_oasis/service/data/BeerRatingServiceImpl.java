package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ItemRatingMapper;
import com.example.hop_oasis.dto.ItemRatingDto;
import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.BeerRating;
import com.example.hop_oasis.repository.BeerRatingRepository;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.service.BeerRatingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BeerRatingServiceImpl implements BeerRatingService {

  private final BeerRatingRepository beerRatingRepository;
  private final BeerRepository beerRepository;
  private final ItemRatingMapper itemRatingMapper;

  @Transactional
  public void addRating(Long beerId, double ratingValue) {
    Beer beer = beerRepository.findById(beerId)
        .orElseThrow(() -> new IllegalArgumentException("Beer not found with id " + beerId));
    BeerRating beerRating = new BeerRating();
    beerRating.setBeer(beer);
    beerRating.setRatingValue(ratingValue);
    beerRatingRepository.save(beerRating);
  }

  @Override
  public double getAverageRating(Long beerId) {
    List<BeerRating> beerRatings = beerRatingRepository.findByBeerId(beerId);
    return beerRatings.stream()
        .mapToDouble(BeerRating::getRatingValue)
        .average()
        .orElse(0.0);
  }

  @Override
  public int getRatingCount(Long beerId) {
    return beerRatingRepository.countByBeerId(beerId);
  }

  @Override
  public ItemRatingDto getItemRating(Long beerId) {
    double averageRating = getAverageRating(beerId);
    int ratingCount = getRatingCount(beerId);
    return itemRatingMapper.toDto(beerId, averageRating, ratingCount);
  }
}
