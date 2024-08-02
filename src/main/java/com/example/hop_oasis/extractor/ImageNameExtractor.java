package com.example.hop_oasis.extractor;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ImageNameExtractor {

  public static String extractName(String name) {
    if (name.startsWith("http://") || name.startsWith("https://")) {
      return name.substring(name.lastIndexOf('/') + 1);
    } else {
      return name;
    }
  }
}
