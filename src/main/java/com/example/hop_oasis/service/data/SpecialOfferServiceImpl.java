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

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientException;
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

    public SpecialOfferProduct createSpecialOffer(String name) {
        return specialOfferRepository.save(SpecialOfferProduct.builder()
                .name(name)
                .build());
    }

    public SpecialOfferAllProductDto getSpecialOffer(Long offerId) {
        SpecialOfferProduct specialOfferProduct = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        return SpecialOfferAllProductDto.builder()
                .id(specialOfferProduct.getId())
                .name(specialOfferProduct.getName())
                .active(specialOfferProduct.isActive())
                .specialOfferBeers(beerInfoMapper.toDtos(specialOfferProduct.getBeers()))
                .specialOfferCiders(ciderInfoMapper.toDtos(specialOfferProduct.getCiders()))
                .specialOfferSnacks(snackInfoMapper.toDtos(specialOfferProduct.getSnacks()))
                .specialOfferProductBundles(productBundleInfoMapper.toDtos(specialOfferProduct.getProductBundles()))
                .build();
    }

    public List<SpecialOfferAllProductDto> getAllSpecialOffers() {
        List<Long> specialOfferProducts = specialOfferRepository.findAllIds();
        List<SpecialOfferAllProductDto> specialOfferAllProductDtos = new ArrayList<>();
        for (Long id : specialOfferProducts) {
            specialOfferAllProductDtos.add(getSpecialOffer(id));
        }
        return specialOfferAllProductDtos;
    }

    public void addBeerToSpecialOffer(Long beerId, Long offerId) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, beerId));

        SpecialOfferProduct specialOfferProduct = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        try {
            beer.getSpecialOfferProduct().add(specialOfferProduct);

            specialOfferRepository.save(specialOfferProduct);
            beerRepository.save(beer);

        } catch (Exception e) {
            throw new ResourceNotFoundException("Beer already added to special offer", beerId);
        }

    }

    public void addCiderToSpecialOffer(Long ciderId, Long offerId) {
        SpecialOfferProduct specialOfferProduct = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));

        Cider cider = ciderRepository.findById(ciderId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, ciderId));
        try {
            cider.getSpecialOfferProduct().add(specialOfferProduct);
            specialOfferRepository.save(specialOfferProduct);
            ciderRepository.save(cider);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Cider already added to special offer", ciderId);
        }
    }

    public void addSnackToSpecialOffer(Long snackId, Long offerId) {
        SpecialOfferProduct specialOfferProduct = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));

        Snack snack = snackRepository.findById(snackId).orElseThrow(() ->
                new ResourceNotFoundException(RESOURCE_NOT_FOUND, snackId));
        try {
            snack.getSpecialOfferProduct().add(specialOfferProduct);
            specialOfferRepository.save(specialOfferProduct);
            snackRepository.save(snack);

        } catch (Exception e) {
            throw new ResourceNotFoundException("Snack already added to special offer", snackId);
        }
    }

    public void addProductBundleToSpecialOffer(Long productBundleId, Long offerId) {
        SpecialOfferProduct specialOfferProduct = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));

        ProductBundle productBundle = productBundleRepository.findById(productBundleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(RESOURCE_NOT_FOUND, productBundleId));
        try {
            productBundle.getSpecialOfferProduct().add(specialOfferProduct);
            specialOfferRepository.save(specialOfferProduct);
            productBundleRepository.save(productBundle);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Product bundle already added to special offer", productBundleId);
        }
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

    public void deleteSnackFromSpecialOffer(Long snackId, Long offerId) {
        SpecialOfferProduct offer = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        offer.getSnacks().removeIf(snack -> snackId.equals(snack.getId()));
        specialOfferRepository.save(offer);
    }

    public void deleteProductBundleFromSpecialOffer(Long productBundleId, Long offerId) {
        SpecialOfferProduct offer = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        offer.getProductBundles().removeIf(productBundle
                -> productBundleId.equals(productBundle.getId()));
        specialOfferRepository.save(offer);

    }

    public SpecialOfferAllProductDto activateSpecialOffer(Long offerId) {
        List<SpecialOfferProduct> allSpecialOffers = specialOfferRepository.findAll();
        for (SpecialOfferProduct offer : allSpecialOffers) {
            if (offer.getId().equals(offerId)) {
                offer.setActive(true);
            } else {
                offer.setActive(false);
            }
            specialOfferRepository.save(offer);
        }
        return getActiveSpecialOffer();
    }

    public SpecialOfferAllProductDto getActiveSpecialOffer() {
        Long id = specialOfferRepository.findActiveSpecialOfferProductIds();
        if (id == null) {
            return new SpecialOfferAllProductDto();
        }
        return getSpecialOffer(id);
    }

    public SpecialOfferAllProductDto updateSpecialOfferName(Long offerId, String name) {
        SpecialOfferProduct offer = specialOfferRepository.findById(offerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(RESOURCE_NOT_FOUND, offerId));
        offer.setName(name);
        specialOfferRepository.save(offer);
        return getSpecialOffer(offerId);
    }
}

