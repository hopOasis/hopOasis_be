package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.SpecialOfferAllProductDto;
import com.example.hop_oasis.dto.SpecialOfferRequestDto;
import com.example.hop_oasis.model.SpecialOfferProduct;
import com.example.hop_oasis.service.data.SpecialOfferServiceImpl;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/special-offers")
@MultipartConfig
@SessionAttributes("specialOfferProduct")
public class SpecialOfferAllProductController {
    private final SpecialOfferServiceImpl specialOfferService;


    @GetMapping("/active")
    public ResponseEntity<SpecialOfferAllProductDto> getActiveSpecialOffer() {
        return ResponseEntity.ok().body(specialOfferService.getActiveSpecialOffer());
    }

    @PutMapping("/{offerId}")
    public ResponseEntity<SpecialOfferAllProductDto> activateSpecialOffer(@PathVariable("offerId") Long offerId) {
        return ResponseEntity.ok().body(specialOfferService.activateSpecialOffer(offerId));
    }

    @PostMapping
    public ResponseEntity<SpecialOfferProduct> createSpecialOffer(@RequestBody
                                                                      SpecialOfferRequestDto specialOfferRequestDto) {
        String name = specialOfferRequestDto.getName();
        return ResponseEntity.ok().body(specialOfferService.createSpecialOffer(name));
    }
    @PutMapping("/name/{offerId}")
    public ResponseEntity<SpecialOfferAllProductDto> updateSpecialOfferName(
            @PathVariable("offerId") Long offerId, @RequestBody SpecialOfferRequestDto specialOfferRequestDto) {
        String name = specialOfferRequestDto.getName();
        return ResponseEntity.ok().body(specialOfferService.updateSpecialOfferName(offerId, name));
    }

    @GetMapping("/{offerId}")
    public SpecialOfferAllProductDto getSpecialOffer(@PathVariable("offerId") Long offerId) {
        return specialOfferService.getSpecialOffer(offerId);
    }

    @GetMapping
    public ResponseEntity<List<SpecialOfferAllProductDto>> getAllSpecialOffers() {
        return ResponseEntity.ok().body(specialOfferService.getAllSpecialOffers());
    }

    @GetMapping("/{offerId}/beers/{beerId}")
    public ResponseEntity<String> addBeerToSpecialOffer(
            @PathVariable("beerId") Long beerId, @PathVariable("offerId") Long offerId) {
        specialOfferService.addBeerToSpecialOffer(beerId, offerId);
        return ResponseEntity.ok().body("Added beer to special offer");
    }

    @GetMapping("/{offerId}/ciders/{ciderId}")
    public void addCiderToSpecialOffer(@PathVariable("ciderId") Long ciderId, @PathVariable("offerId") Long offerId) {
        specialOfferService.addCiderToSpecialOffer(ciderId, offerId);
    }

    @GetMapping("/{offerId}/snacks/{snackId}")
    public void addSnackToSpecialOffer(@PathVariable("snackId") Long snackId, @PathVariable("offerId") Long offerId) {
        specialOfferService.addSnackToSpecialOffer(snackId, offerId);
    }

    @GetMapping("/{offerId}/products-bundle/{productBundleId}")
    public void addProductBundleToSpecialOffer(@PathVariable("productBundleId") Long productBundleId,
                                               @PathVariable("offerId") Long offerId) {
        specialOfferService.addProductBundleToSpecialOffer(productBundleId, offerId);
    }

    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteSpecialOffer(@PathVariable("offerId") Long offerId) {
        specialOfferService.deleteSpecialOffer(offerId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{offerId}/beers/{beerId}")
    public void deleteBeerFromSpecialOffer(@PathVariable("beerId") Long beerId,
                                           @PathVariable("offerId") Long offerId) {
        specialOfferService.deleteBeerFromSpecialOffer(beerId, offerId);
    }

    @DeleteMapping("/{offerId}/ciders/{ciderId}")
    public void deleteCiderFromSpecialOffer(@PathVariable("ciderId") Long ciderId,
                                            @PathVariable("offerId") Long offerId) {
        specialOfferService.deleteCiderFromSpecialOffer(ciderId, offerId);
    }

    @DeleteMapping("/{offerId}/snacks/{snackId}")
    public void deleteSnackFromSpecialOffer(@PathVariable("snackId") Long snackId,
                                            @PathVariable("offerId") Long offerId) {
        specialOfferService.deleteSnackFromSpecialOffer(snackId, offerId);
    }

    @DeleteMapping("/{offerId}/products-bundle/{productBundleId}")
    public void deleteProductBundleFromSpecialOffer(@PathVariable("productBundleId") Long productBundleId,
                                                    @PathVariable("offerId") Long offerId) {
        specialOfferService.deleteProductBundleFromSpecialOffer(productBundleId, offerId);
    }


}
