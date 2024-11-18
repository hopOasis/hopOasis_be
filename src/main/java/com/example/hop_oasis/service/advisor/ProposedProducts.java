package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.Snack;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProposedProducts {

    private List<Beer> beers = new ArrayList<>();

    private List<Cider> ciders = new ArrayList<>();

    private List<Snack> snacks = new ArrayList<>();

    private List<ProductBundle> bundles = new ArrayList<>();

}
