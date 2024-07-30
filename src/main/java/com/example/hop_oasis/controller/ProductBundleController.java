package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.ProductBundleInfoMapper;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.service.ProductBundleImageService;
import com.example.hop_oasis.service.ProductBundleService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products-bundle")
@MultipartConfig
@Validated
public class ProductBundleController {
    private final ProductBundleService productBundleService;
    private final ProductBundleImageService imageService;
    private final ProductBundleInfoMapper productBundleInfoMapper;
    @GetMapping
    public ResponseEntity<Page<ProductBundleInfoDto>> getAllProductBundles(@RequestParam(value =
                                                                           "page",defaultValue = "0") int page,
                                                                           @RequestParam(value =
                                                                                   "size",defaultValue = "10") int size) {
        Page<ProductBundleInfoDto> productBundlePage =
                productBundleService.getAllProductBundle(PageRequest.of(page, size));
        return ResponseEntity.ok().body(productBundlePage);
    }
    @PostMapping
    public ResponseEntity<ProductBundleInfoDto> saveProductBundle(@RequestBody ProductBundleDto productBundleDto) {
        ProductBundleInfoDto dto = productBundleInfoMapper
                .toDto(productBundleService.saveProductBundle(productBundleDto));
        return ResponseEntity.ok().body(dto);
    }
    @PostMapping(path = "/{id}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProductBundleInfoDto> addImageToProductBundle(@PathVariable("id") Long id,
                                                          @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok().body(imageService.addProductBundleImage(id, image));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductBundleInfoDto> getProductBundleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(productBundleService.getProductBundleById(id));
    }
    @PostMapping("/{id}/ratings")
    public ResponseEntity<?> addRating(@PathVariable("id") Long id, @Valid @RequestBody RatingDto ratingDto) {
        try {
            double ratingValue = ratingDto.getRatingValue();
            ProductBundleInfoDto dto = productBundleService.addRatingAndReturnUpdatedProductBundleInfo(id, ratingValue);
            return ResponseEntity.ok().body(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductBundleInfoDto> updateProductBundle(@RequestParam("id") Long id,
                                                    @RequestBody ProductBundleInfoDto productBundleInfo) {
        return ResponseEntity.ok().body(productBundleService.update(productBundleInfo, id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductBundleInfoDto> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(productBundleService.deleteProductBundle(id));
    }
    @DeleteMapping("/images")
    public ResponseEntity<String> deleteImage(@RequestBody ImageUrlDto name) {
        imageService.deleteProductBundleImage(name.getName());
        return ResponseEntity.ok().body(name.getName());
    }
}
