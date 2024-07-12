package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.*;
import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.SpecialOfferAllProductDto;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@SessionAttributes("specialOfferProduct")
public class SpecialOfferServiceImpl {
    private final SpecialOfferRepository specialOfferRepository;
    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;
    private final ProductBundleRepository productBundleRepository;
    private final BeerInfoMapper beerInfoMapper;
    private final CiderInfoMapper ciderInfoMapper;
    private final SnackInfoMapper snackInfoMapper;
    private final ProductBundleInfoMapper productBundleInfoMapper;

    public Long createSpecialOffer() {
        return specialOfferRepository.save(SpecialOfferProduct.builder().build()).getId();
    }
    public SpecialOfferAllProductDto getSpecialOffer(Long offerId) {
       SpecialOfferProduct  specialOfferProduct = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        return SpecialOfferAllProductDto.builder()
                .id(specialOfferProduct.getId())
                .specialOfferBeers(beerInfoMapper.toDtos(specialOfferProduct.getBeers()))
                .specialOfferCiders(ciderInfoMapper.toDtos(specialOfferProduct.getCiders()))
                .specialOfferSnacks(snackInfoMapper.toDtos(specialOfferProduct.getSnacks()))
                .specialOfferProductBundles(productBundleInfoMapper.toDtos(specialOfferProduct.getProductBundles()))
                .build();
    }
    public void addBeerToSpecialOffer(Long beerId, Long offerId) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, beerId));

        SpecialOfferProduct specialOfferProduct = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));

        beer.getSpecialOfferProduct().add(specialOfferProduct);

        specialOfferRepository.save(specialOfferProduct);
        beerRepository.save(beer);
    }
    public void addCiderToSpecialOffer(Long ciderId, Long offerId) {
        SpecialOfferProduct specialOfferProduct = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));

        Cider cider  = ciderRepository.findById(ciderId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, ciderId));
        cider.getSpecialOfferProduct().add(specialOfferProduct);
        specialOfferRepository.save(specialOfferProduct);
        ciderRepository.save(cider);
    }
    public void addSnackToSpecialOffer(Long snackId, Long offerId) {
        SpecialOfferProduct specialOfferProduct = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));

        Snack snack = snackRepository.findById(snackId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, snackId));

        snack.getSpecialOfferProduct().add(specialOfferProduct);
        specialOfferRepository.save(specialOfferProduct);
        snackRepository.save(snack);
    }
    public void addProductBundleToSpecialOffer(Long productBundleId, Long offerId) {
        SpecialOfferProduct specialOfferProduct = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));

      ProductBundle productBundle = productBundleRepository.findById(productBundleId)
              .orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, productBundleId));

        productBundle.getSpecialOfferProduct().add(specialOfferProduct);
        specialOfferRepository.save(specialOfferProduct);
        productBundleRepository.save(productBundle);
    }
    public void deleteSpecialOffer(Long offerId) {
        specialOfferRepository.deleteById(offerId);
    }
    public void deleteBeerFromSpecialOffer(Long beerId, Long offerId) {
       SpecialOfferProduct offer = specialOfferRepository.findById(offerId)
               .orElseThrow(() ->
               new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        offer.getBeers().removeIf(beer -> beerId.equals(beer.getId()));
        specialOfferRepository.save(offer);
    }
    public void deleteCiderFromSpecialOffer(Long ciderId, Long offerId) {
        SpecialOfferProduct offer = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        offer.getCiders().removeIf(cider -> ciderId.equals(cider.getId()));
        specialOfferRepository.save(offer);
    }
    public void deleteSnackFromSpecialOffer(Long snackId,Long offerId) {
        SpecialOfferProduct offer = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        offer.getSnacks().removeIf(snack -> snackId.equals(snack.getId()));
        specialOfferRepository.save(offer);
    }
    public void deleteProductBundleFromSpecialOffer(Long productBundleId,Long offerId) {
        SpecialOfferProduct offer = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        offer.getProductBundles().removeIf(productBundle
                -> productBundleId.equals(productBundle.getId()));
        specialOfferRepository.save(offer);

    }
}
