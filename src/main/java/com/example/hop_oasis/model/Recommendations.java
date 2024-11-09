package com.example.hop_oasis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Recommendations {
    private List<Beer> beers;
    private List<Cider> ciders;
    private List<Snack> snacks;
    private List<ProductBundle> bundles;
}
