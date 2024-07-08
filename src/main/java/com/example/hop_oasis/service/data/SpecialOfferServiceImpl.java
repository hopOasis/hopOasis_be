package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.*;
import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.SpecialOfferAllProductDto;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
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


   private  SpecialOfferProduct specialOfferProduct = createSpecialOffer();

    public SpecialOfferProduct createSpecialOffer() {
        SpecialOfferProduct offer = SpecialOfferProduct.builder().build();
        return offer;
    }
    public SpecialOfferAllProductDto saveSpecialOffer() {
        specialOfferProduct = specialOfferRepository.save(specialOfferProduct);
         return SpecialOfferAllProductDto.builder()
                .id(specialOfferProduct.getId())
                .specialOfferBeers(beerInfoMapper.toDtos(specialOfferProduct.getBeers()))
                .specialOfferCiders(ciderInfoMapper.toDtos(specialOfferProduct.getCiders()))
                .specialOfferSnacks(snackInfoMapper.toDtos(specialOfferProduct.getSnacks()))
                .specialOfferProductBundles(productBundleInfoMapper.toDtos(specialOfferProduct.getProductBundles()))
                .build();


    }
    public SpecialOfferAllProductDto getSpecialOffer(Long offerId) {
        SpecialOfferProduct specialOfferProduct = specialOfferRepository.findById(offerId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        return SpecialOfferAllProductDto.builder()
                .id(specialOfferProduct.getId())
                .specialOfferBeers(beerInfoMapper.toDtos(specialOfferProduct.getBeers()))
                .specialOfferCiders(ciderInfoMapper.toDtos(specialOfferProduct.getCiders()))
                .specialOfferSnacks(snackInfoMapper.toDtos(specialOfferProduct.getSnacks()))
                .specialOfferProductBundles(productBundleInfoMapper.toDtos(specialOfferProduct.getProductBundles()))
                .build();


    }


    public void addBeerToSpecialOffer(Long beerId) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, beerId));
        specialOfferProduct.getBeers().add(beer);
    }
    public void addCiderToSpecialOffer(Long ciderId) {
        Cider cider  = ciderRepository.findById(ciderId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, ciderId));
        specialOfferProduct.getCiders().add(cider);
    }
    public void addSnackToSpecialOffer(Long snackId) {
        Snack snack = snackRepository.findById(snackId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, snackId));
        specialOfferProduct.getSnacks().add(snack);
    }
    public void addProductBundleToSpecialOffer(Long productBundleId) {
      ProductBundle productBundle = productBundleRepository.findById(productBundleId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, productBundleId));
        specialOfferProduct.getProductBundles().add(productBundle);
    }
    public void deleteSpecialOffer(Long offerId) {
        specialOfferRepository.deleteById(offerId);
    }
    public void deleteBeerFromSpecialOfferBeforeSave(Long beerId) {
        specialOfferProduct.getBeers().removeIf(beer -> beerId.equals(beer.getId()));
    }
    public void deleteCiderFromSpecialOfferBeforeSave(Long ciderId) {
        specialOfferProduct.getCiders().removeIf(cider -> ciderId.equals(cider.getId()));
    }
    public void deleteSnackFromSpecialOfferBeforeSave(Long snackId) {
        specialOfferProduct.getSnacks().removeIf(snack -> snackId.equals(snack.getId()));
    }
    public void deleteProductBundleFromSpecialOfferBeforeSave(Long productBundleId) {
        specialOfferProduct.getProductBundles().removeIf(productBundle
                -> productBundle.getId().equals(productBundleId));
    }
    public void deleteBeerFromSpecialOfferAfterSave(Long beerId, Long offerId) {
       SpecialOfferProduct offer = specialOfferRepository.findById(offerId).orElseThrow(() ->
               new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        offer.getBeers().removeIf(beer -> beerId.equals(beer.getId()));
        specialOfferRepository.save(offer);
    }
    public void deleteCiderFromSpecialOfferAfterSave(Long ciderId, Long offerId) {
        SpecialOfferProduct offer = specialOfferRepository.findById(offerId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        offer.getCiders().removeIf(cider -> ciderId.equals(cider.getId()));
        specialOfferRepository.save(offer);
    }

    public void deleteSnackFromSpecialOfferAfterSave(Long snackId,Long offerId) {
        SpecialOfferProduct offer = specialOfferRepository.findById(offerId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        offer.getSnacks().removeIf(snack -> snackId.equals(snack.getId()));
        specialOfferRepository.save(offer);
    }
    public void deleteProductBundleFromSpecialOfferAfterSave(Long productBundleId,Long offerId) {
        SpecialOfferProduct offer = specialOfferRepository.findById(offerId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        offer.getProductBundles().removeIf(productBundle
                -> productBundleId.equals(productBundle.getId()));
        specialOfferRepository.save(offer);

    }
}
