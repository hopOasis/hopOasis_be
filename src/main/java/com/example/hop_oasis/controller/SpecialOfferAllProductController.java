package com.example.hop_oasis.controller;
import com.example.hop_oasis.dto.SpecialOfferAllProductDto;
import com.example.hop_oasis.service.data.SpecialOfferServiceImpl;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/special-offers")
@MultipartConfig
@SessionAttributes("specialOfferProduct")
public class SpecialOfferAllProductController {
    private final SpecialOfferServiceImpl specialOfferService;

    @GetMapping("/create")
    public ResponseEntity<Long> createSpecialOffer() {
        return ResponseEntity.ok().body(specialOfferService.createSpecialOffer());
    }

    @GetMapping("/{offerId}")
    public SpecialOfferAllProductDto getSpecialOffer(@PathVariable("offerId") Long offerId) {
        return specialOfferService.getSpecialOffer(offerId);
    }

    @GetMapping("/beer/{beerId}/{offerId}")
    public ResponseEntity<String> addBeerToSpecialOffer(@PathVariable("beerId") Long beerId
                                                        , @PathVariable("offerId") Long offerId) {
        specialOfferService.addBeerToSpecialOffer(beerId, offerId);
        return ResponseEntity.ok().body("Added beer to special offer");
    }

    @GetMapping("/cider/{ciderId}/{offerId}")
    public void addCiderToSpecialOffer(@PathVariable("ciderId") Long ciderId
                                       , @PathVariable("offerId") Long offerId) {
        specialOfferService.addCiderToSpecialOffer(ciderId, offerId);
    }
    @GetMapping("/snack/{snackId}/{offerId}")
    public void addSnackToSpecialOffer(@PathVariable("snackId") Long snackId
                                       , @PathVariable("offerId") Long offerId) {
        specialOfferService.addSnackToSpecialOffer(snackId, offerId);
    }
    @GetMapping("/product-bundle/{productBundleId}/{offerId}")
    public void addProductBundleToSpecialOffer(@PathVariable("productBundleId") Long productBundleId
                                               , @PathVariable("offerId") Long offerId) {
        specialOfferService.addProductBundleToSpecialOffer(productBundleId, offerId);
    }
    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteSpecialOffer(@PathVariable("offerId") Long offerId) {
        specialOfferService.deleteSpecialOffer(offerId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/beer/{beerId}/{offerId}")
    public void deleteBeerFromSpecialOffer(@PathVariable("beerId") Long beerId
                                           , @PathVariable("offerId") Long offerId) {
        specialOfferService.deleteBeerFromSpecialOffer(beerId, offerId);
    }
    @DeleteMapping("/cider/{ciderId}/{offerId}")
    public void deleteCiderFromSpecialOffer(@PathVariable("ciderId") Long ciderId
                                            , @PathVariable("offerId") Long offerId) {
        specialOfferService.deleteCiderFromSpecialOffer(ciderId, offerId);
    }
    @DeleteMapping("/snack/{snackId}/{offerId}")
    public void deleteSnackFromSpecialOffer(@PathVariable("snackId") Long snackId
                                            , @PathVariable("offerId") Long offerId) {
        specialOfferService.deleteSnackFromSpecialOffer(snackId, offerId);
    }
    @DeleteMapping("/product-bundle/{productBundleId}/{offerId}")
    public void deleteProductBundleFromSpecialOffer(@PathVariable("productBundleId") Long productBundleId
                                                    , @PathVariable("offerId") Long offerId) {
        specialOfferService.deleteProductBundleFromSpecialOffer(productBundleId, offerId);
    }


}
