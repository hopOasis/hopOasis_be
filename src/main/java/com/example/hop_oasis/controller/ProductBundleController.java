package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleImageDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.service.ProductBundleImageService;
import com.example.hop_oasis.service.ProductBundleService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products-bundle")
@MultipartConfig
public class ProductBundleController {
    private final ProductBundleService productBundleService;
    private final ProductBundleImageService imageService;
    @GetMapping
    public ResponseEntity<List<ProductBundleInfoDto>> getAllProductBundles() {
        return ResponseEntity.ok().body(productBundleService.getAllProductBundle());
    }
    @PostMapping
    public ResponseEntity<Void> saveProductBundle(@RequestParam("name") String name,
                                    @RequestParam("price") double price,
                                    @RequestParam("description") String description,
                                    @RequestParam("image") MultipartFile image) {
        ProductBundleDto productBundleDto = new ProductBundleDto();
        productBundleDto.setName(name);
        productBundleDto.setPrice(price);
        productBundleDto.setDescription(description);
        productBundleService.saveProductBundle(image, productBundleDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/add/image")
    public ResponseEntity<Void> addImageToProductBundle(@RequestParam("id") Long id,
                                          @RequestParam("image") MultipartFile image) {
        imageService.addProductBundleImage(id, image);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductBundleInfoDto> getProductBundleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(productBundleService.getProductBundleById(id));
    }
    @GetMapping("/images/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name) {
        ProductBundleImageDto imageDto = imageService.getProductBundleImage(name);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageDto.getImage());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProductBundle(@RequestParam("id") Long id,
                                                    @RequestBody ProductBundleInfoDto productBundleInfo) {
        productBundleService.update(productBundleInfo, id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        productBundleService.deleteProductBundle(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/images/{name}")
    public ResponseEntity<Void> deleteImage(@PathVariable("name") String name) {
        imageService.deleteProductBundleImage(name);
        return ResponseEntity.ok().build();
    }
}
