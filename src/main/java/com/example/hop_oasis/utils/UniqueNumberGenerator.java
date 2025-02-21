package com.example.hop_oasis.utils;

import java.util.Random;

public class UniqueNumberGenerator {
    public static String genStr() {
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(48, 58)
                .limit(targetStringLength)
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

}
