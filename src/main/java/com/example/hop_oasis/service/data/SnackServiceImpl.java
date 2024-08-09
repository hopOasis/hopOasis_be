package com.example.hop_oasis.service.data;
import com.example.hop_oasis.convertor.SnackInfoMapper;
import com.example.hop_oasis.convertor.SnackMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Snack;
import com.example.hop_oasis.repository.SnackImageRepository;
import com.example.hop_oasis.repository.SnackRepository;
import com.example.hop_oasis.service.SnackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class SnackServiceImpl implements SnackService {
    private final SnackRepository snackRepository;
    private final SnackImageRepository snackImageRepository;
    private final SnackMapper snackMapper;
    private final SnackInfoMapper snackInfoMapper;
    private final SnackRatingServiceImpl snackRatingService;
    @Override
    public Snack saveSnack(SnackDto snackDto) {
        Snack snack = snackMapper.toEntity(snackDto);
        snackRepository.save(snack);
        return snack;
    }
    @Override
    public SnackInfoDto getSnackById(Long id) {
        Snack snack = snackRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));
        return convertToDtoWithRating(snack);
    }
    @Override
    public SnackInfoDto addRatingAndReturnUpdatedSnackInfo(Long id, double ratingValue) {

        snackRatingService.addRating(id, ratingValue);
        Snack snack = snackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Snack not found with id " + id));
        return convertToDtoWithRating(snack);
    }
    private SnackInfoDto convertToDtoWithRating(Snack snack) {
        SnackInfoDto snackInfoDto = snackInfoMapper.toDto(snack);
        ItemRatingDto rating = snackRatingService.getItemRating(snack.getId());
        BigDecimal roundedAverageRating = BigDecimal.valueOf(rating.getAverageRating())
                .setScale(1, RoundingMode.HALF_UP);
        snackInfoDto.setAverageRating(roundedAverageRating.doubleValue());
        snackInfoDto.setRatingCount(rating.getRatingCount());
        return snackInfoDto;
    }
    @Override
    public Page<SnackInfoDto> getAllSnacks(Pageable pageable) {
        Page<Snack> snacks = snackRepository.findAll(pageable);
        if (snacks.isEmpty()) {
          return Page.empty(pageable);
        }
        return snacks.map(this::convertToDtoWithRating);
    }
    @Override
    public SnackInfoDto updateSnack(SnackInfoDto snackInfo, Long id) {
        Snack snack = snackRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, id));

        if (!snackInfo.getSnackName().isEmpty()) {
            snack.setSnackName(snackInfo.getSnackName());
        }
        if (snackInfo.getWeightLarge() != 0.0) {
            snack.setWeightLarge(snackInfo.getWeightLarge());
        }
        if (snackInfo.getWeightSmall() != 0.0) {
            snack.setWeightSmall(snackInfo.getWeightSmall());
        }
        if (snackInfo.getPriceLarge() != 0.0) {
            snack.setPriceLarge(snackInfo.getPriceLarge());
        }
        if (snackInfo.getPriceSmall() != 0.0) {
            snack.setPriceSmall(snackInfo.getPriceSmall());
        }
        if (!snackInfo.getDescription().isEmpty()) {
            snack.setDescription(snackInfo.getDescription());
        }
       return snackInfoMapper.toDto(snackRepository.save(snack));
    }
    @Override
    public SnackInfoDto deleteSnack(Long id) {
        Snack snack = snackRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(RESOURCE_DELETED, id));
        snackRepository.deleteById(id);
        return snackInfoMapper.toDto(snack);
    }
}
