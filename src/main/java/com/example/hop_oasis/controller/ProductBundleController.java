package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.ProductBundleInfoMapper;
import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleImageDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.model.ProductBundleImage;
import com.example.hop_oasis.service.ProductBundleImageService;
import com.example.hop_oasis.service.ProductBundleService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
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
    private final ProductBundleInfoMapper productBundleInfoMapper;
    @GetMapping
    public ResponseEntity<Page<ProductBundleInfoDto>> getAllProductBundles(@RequestParam(value =
                                                                           "page",defaultValue = "0") int page,
                                                                           @RequestParam(value =
                                                                                   "size",defaultValue = "10") int size) {
        Page<ProductBundleInfoDto> productBundlePage =
                productBundleService.getAllProductBundle(PageRequest.of(page, size));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", "items " + page * size + "-" + ((page + 1) * size - 1)
                + "/" + productBundlePage.getTotalElements());
        return ResponseEntity.ok().headers(headers).body(productBundlePage);
    }
    @PostMapping
    public ResponseEntity<ProductBundleInfoDto> saveProductBundle(@RequestParam("name") String name,
                                    @RequestParam("price") double price,
                                    @RequestParam("description") String description,
                                    @RequestParam("image") MultipartFile image) {
        ProductBundleDto productBundleDto = new ProductBundleDto();
        productBundleDto.setName(name);
        productBundleDto.setPrice(price);
        productBundleDto.setDescription(description);

        return ResponseEntity.ok().body(productBundleInfoMapper
                .toDto(productBundleService.saveProductBundle(image, productBundleDto)));
    }
    @PostMapping("/add/image")
    public ResponseEntity<byte[]> addImageToProductBundle(@RequestParam("id") Long id,
                                          @RequestParam("image") MultipartFile image) {
        ProductBundleImage i = imageService.addProductBundleImage(id, image);
        ProductBundleImageDto imageDto = imageService.getProductBundleImage(i.getName());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageDto.getImage());
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
    public ResponseEntity<ProductBundleInfoDto> updateProductBundle(@RequestParam("id") Long id,
                                                    @RequestBody ProductBundleInfoDto productBundleInfo) {
        return ResponseEntity.ok().body(productBundleService.update(productBundleInfo, id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductBundleInfoDto> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(productBundleService.deleteProductBundle(id));
    }
    @DeleteMapping("/images/{name}")
    public ResponseEntity<String> deleteImage(@PathVariable("name") String name) {
        imageService.deleteProductBundleImage(name);
        return ResponseEntity.ok().body(name);
    }
}
