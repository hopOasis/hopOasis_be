package com.example.hop_oasis.utils;

import com.example.hop_oasis.dto.CartItemDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class WeightCalculatorService {

    public BigDecimal calculateTotalWeight(List<CartItemDto> items) {
        BigDecimal totalWeightKg = BigDecimal.ZERO;

        for (CartItemDto item : items) {
            BigDecimal itemWeightKg = BigDecimal.ZERO;

            switch (item.getItemType()) {
                case BEER:
                case CIDER:
                    itemWeightKg = BigDecimal.valueOf(item.getMeasureValue());
                    break;
                case SNACK:
                    itemWeightKg = BigDecimal.valueOf(item.getMeasureValue())
                            .divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
                    break;
                case PRODUCT_BUNDLE:
                    continue;
            }

            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            totalWeightKg = totalWeightKg.add(itemWeightKg.multiply(quantity));
        }

        return totalWeightKg;
    }
}