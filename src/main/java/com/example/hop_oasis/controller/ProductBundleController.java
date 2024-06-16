package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleImageDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.service.data.ProductBundleImageServiceImpl;
import com.example.hop_oasis.service.data.ProductBundleServiceImpl;
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
    private final ProductBundleServiceImpl productBundleService;
    private final ProductBundleImageServiceImpl imageService;
    @GetMapping
    public ResponseEntity<List<ProductBundleInfoDto>> getAllProductBundles() {
        return ResponseEntity.ok().body(productBundleService.getAllProductBundle());
    }
    @PostMapping
    public String saveProductBundle(@RequestParam("name") String name,
                                    @RequestParam("price") double price,
                                    @RequestParam("description") String description,
                                    @RequestParam("image") MultipartFile image) {
        ProductBundleDto productBundleDto = new ProductBundleDto();
        productBundleDto.setName(name);
        productBundleDto.setPrice(price);
        productBundleDto.setDescription(description);
        productBundleService.saveProductBundle(image, productBundleDto);
        return "Done";
    }
    @PostMapping("/add/image")
    public String addImageToProductBundle(@RequestParam("id") Long id,
                                          @RequestParam("image") MultipartFile image) {
        imageService.addProductBundleImage(id, image);
        return "Done";
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductBundleInfoDto> getProductBundleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(productBundleService.getProductBundleById(id));
    }
    @GetMapping("/image/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name) {
        ProductBundleImageDto imajeDto = imageService.getProductBundleImage(name);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imajeDto.getImage());
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProductBundle(@RequestParam("id") Long id,
                                                      @RequestBody ProductBundleInfoDto productBundleInfo) {
        productBundleService.update(productBundleInfo, id);
        return ResponseEntity.ok().body("Done");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        productBundleService.deleteProductBundle(id);
        return ResponseEntity.ok().body("Done");
    }
    @DeleteMapping("/image/{name}")
    public ResponseEntity<String> deleteImage(@PathVariable("name") String name) {
        imageService.deleteProductBundleImage(name);
        return ResponseEntity.ok().body("Done");
    }
}
