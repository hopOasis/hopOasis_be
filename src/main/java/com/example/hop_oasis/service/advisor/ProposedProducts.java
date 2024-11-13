package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.Snack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProposedProducts {
    private List<Beer> beers;
    private List<Cider> ciders;
    private List<Snack> snacks;
    private List<ProductBundle> bundles;
}
