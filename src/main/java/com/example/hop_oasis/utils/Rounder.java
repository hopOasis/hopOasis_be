package com.example.hop_oasis.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Rounder {
    public static BigDecimal roundValue(double value) {
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    public static double roundDoubleValue(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
